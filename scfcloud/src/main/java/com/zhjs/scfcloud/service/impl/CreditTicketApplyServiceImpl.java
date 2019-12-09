package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.feign.CreditRecordFeignService;
import com.zhjs.scfcloud.feign.CreditTicketApplyFeignService;
import com.zhjs.scfcloud.feign.FileFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import com.zhjs.scfcloud.model.entity.BusinessFlow;
import com.zhjs.scfcloud.model.entity.BusinessType;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.model.vo.credit.PersonalCreditVO;
import com.zhjs.scfcloud.service.CreditTicketApplyService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author: dailongting
 * @date:2019/6/28 17:05
 */
@Service
public class CreditTicketApplyServiceImpl implements CreditTicketApplyService {

    @Autowired
    private CreditRecordFeignService creditRecordFeignService;
    @Autowired
    private CreditTicketApplyFeignService creditTicketApplyFeignService;
    @Autowired
    private FileFeignService fileFeignService;
    @Resource
    private FileTransfer fileTransfer;

    @Override
    public Result findCreditTicketCfg(QueryCreditTicketCfgDTO dto) {
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null){
            return Result.failure("未获取到登录信息");
        }

        Map<String, Object> data = new HashMap<>();
        Long companyId = null;
        BusinessType businessType = null;
        if(dto.getCreditTicketApplyId() == null){

            PersonalCreditVO personalCreditVO = null;
            //如果授信记录ID为空，则取当前登录用户的身份证，获取相关授信记录
            if(dto.getCreditRecordId() == null){
                Result<List<PersonalCreditVO>> result = creditRecordFeignService.findPersonalCreditList(userInfoVO.getIdCard(),userInfoVO.getCompanyId());
                List<PersonalCreditVO> personalCreditVOList = result.getData();
                if(personalCreditVOList == null || personalCreditVOList.isEmpty()){
                    return Result.failure("当前登录用户无可用的授信记录");
                }
                //默认取第一个作为初始开票申请数据
                personalCreditVO = personalCreditVOList.get(0);
                data.put("creditList",personalCreditVOList);
            }else{//如果授信记录ID不为空，则只允许使用当前授信记录进行开票申请
                Result<PersonalCreditVO> result = creditRecordFeignService.findPersonalCredit(dto.getCreditRecordId());
                personalCreditVO = result.getData();
                data.put("creditList", Arrays.asList(personalCreditVO));
            }

            companyId = personalCreditVO.getCompanyId();
            businessType = BusinessCfgUtil.getBusinessType(personalCreditVO.getCompanyId(), "申请开商票");
            if(businessType == null){
                return Result.failure("无申请开商票业务配置数据");
            }
        }else{
            //查询类型不为创建类型，则需要查询相关业务数据
            if(!CommonEnum.QueryType.CREATE.getStatus().equals(dto.getQueryType())){
                Result<Map<String, Object>> detailsData = creditTicketApplyFeignService.findCreditTicketApplyDetails(dto.getCreditTicketApplyId());
                if(detailsData.getCode() != Result.RESULT_CODE_SUCCESS){
                    return detailsData;
                }

                Map<String, Object> detailsDataMap =  detailsData.getData();
                companyId = Long.valueOf(detailsDataMap.get("companyId").toString());
                businessType = BusinessCfgUtil.getBusinessType(companyId,Long.valueOf(detailsDataMap.get("businessTypeId").toString()));
                if(businessType == null){
                    return Result.failure("无申请开商票业务配置数据");
                }

                Result<List<File>> fileData =
                        fileFeignService.findSelective(
                                new FileDTO().setCompanyId(companyId)
                                        .setBusinessTypeId(businessType.getId())
                                        .setBusinessId(dto.getCreditTicketApplyId()));
                if(fileData.getCode() != Result.RESULT_CODE_SUCCESS){
                    return fileData;
                }

                data.put("detailsData", detailsData.getData());
                data.put("fileList", fileData.getData());

            }
        }

        //查询当前流程相关配置
        Result<Map<String, Object>> currentFlowCfg = BusinessCfgUtil.getCurrentFlowCfg(companyId,businessType.getId(),dto.getFlowCode());
        if(currentFlowCfg.getCode() != Result.RESULT_CODE_SUCCESS){
            return currentFlowCfg;
        }
        data.putAll(currentFlowCfg.getData());

        //排除登录用户没有权限的流程扭转配置
        List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.powerFilterWorkFlowCfg((List<BusinessWorkFlowExtendVO>)data.get("trailingWorkFlowCfg"), userInfoVO.getId(), userInfoVO.getRoleList());
        data.put("trailingWorkFlowCfg", trailingWorkFlowCfg);

        //查询详情表单相关配置
        Result<Map<String, Object>> detailsCfg = BusinessCfgUtil.getDetailsCfg(companyId,businessType.getId());
        if(detailsCfg.getCode() != Result.RESULT_CODE_SUCCESS){
            return detailsCfg;
        }

        data.putAll(detailsCfg.getData());

        return Result.success(data);
    }

    @Override
    public Result saveDraft(CreditTicketDraftSaveDTO dto) {
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null){
            return Result.failure("未获取到登录信息");
        }
        dto.setUserId(user.getId());

        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请开商票");
        if(businessType == null){
            return Result.failure("无申请开商票业务配置数据");
        }

        //做业务字段校验
        String[] columnNameList = dto.getColumnName().split(" ");
        String[] columnValList = dto.getValue().split(" ");
        for(int i=0;i<columnNameList.length;i++){
            Result validResult = BusinessCfgUtil.validAttr(dto.getCompanyId()
                    , businessType.getId()
                    , CommonEnum.WhetherEnum.YES.getStatus()
                    , columnNameList[i]
                    , columnValList[i]
                    ,CommonEnum.QueryType.CREATE.getStatus());
            if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
                return validResult;
        }
        return creditTicketApplyFeignService.saveDraft(dto);
    }

    //开票申请审批提交
    @Override
    public Result auditCommit(CommonAuditCommitDTO dto) {
        return creditTicketApplyFeignService.auditCommit(dto);
    }

    @Override
    public Result saveFile(CreditTicketFileSaveDTO dto) {
        UserInfoVO user = MySubjectUtil.getUser();
        if(user == null){
            return Result.failure("未获取到登录信息");
        }
        dto.setUserId(user.getId());

        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请开商票");
        if(businessType == null){
            return Result.failure("无申请开商票业务配置数据");
        }
        //获取审批流程相关配置
        BusinessWorkFlowExtendVO workFlowExtendVO =
                BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getWorkFlowId(), dto.getCompanyId(), businessType.getId());

        //查询历史附件进行附件校验
        Result<List<File>> result =
                fileFeignService.findSelective(
                        new FileDTO().setCompanyId(dto.getCompanyId())
                                .setBusinessTypeId(businessType.getId())
                                .setBusinessId(dto.getBusinessId())
                                .setFileCode(dto.getFile().getFileCode())
                                .setStatus(CommonEnum.StatusEnnum.status1.getStatus())
                );
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return Result.failure("查询历史文件失败");
        }

        List<File> fileList = null;
        if(result.getData() == null){
            fileList = new ArrayList<>();
        }else{
            fileList = result.getData();
        }
        fileList.add(dto.getFile());
        BusinessWorkFlowFileVO workFlowFileCfg = workFlowExtendVO.getWorkFlowFileVOList()
                .stream().filter(workFlowFile -> workFlowFile.getFileCode().equals(dto.getFile().getFileCode())).findAny().orElse(null);
        //校验附件
        Result valid = BusinessCfgUtil.validFile(fileList,workFlowFileCfg);
        if(valid.getCode() != Result.RESULT_CODE_SUCCESS){
            return valid;
        }

        //保存开票申请提交文件
        return creditTicketApplyFeignService.saveFile(dto);
    }

    @Override
    public Result applyCommit(CreditTicketApplyCommitDTO dto) {
        UserInfoVO user = MySubjectUtil.getUser();
        if(user == null){
            return Result.failure("未获取到登录信息");
        }
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return creditTicketApplyFeignService.applyCommit(dto);
    }

    @Override
    public Result findApprovalCourse(CreditTicketApplyBaseDTO dto) {
        return creditTicketApplyFeignService.findApprovalCourse(dto);
    }

    @Override
    public Result getDetails(QueryApplyDetailsDTO dto) {
        return creditTicketApplyFeignService.getDetails(dto);
    }

    /**
     * 审批完成列表
     * @param dto
     * @return
     */
    @Override
    public Result getAuditFinishList(CreditUseApplyAuditFinishListQueryDTO dto) {
        return creditTicketApplyFeignService.getAuditFinishList(dto);
    }

}
