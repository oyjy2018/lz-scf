package com.zhjs.scfcloud.app.service.impl;

import com.zhjs.scfcloud.app.feign.CommentFeignService;
import com.zhjs.scfcloud.app.feign.CreditApplyFeignService;
import com.zhjs.scfcloud.app.feign.FileFeignService;
import com.zhjs.scfcloud.app.service.CreditApplyService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommentDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.CommentVO;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 授信申请
 */
@Service
public class CreditApplyServiceImpl implements CreditApplyService {

    @Autowired
    private CreditApplyFeignService creditApplyFeignService;
    @Autowired
    private FileFeignService fileFeignService;
    @Autowired
    private CommentFeignService commentFeignService;
    @Autowired
    private FileTransfer fileTransfer;

    @Override
    public String saveDraft(CreditApplyDraftSaveDTO creditApplyDraftSaveDTO) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(creditApplyDraftSaveDTO.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据").toJSON();
        }
        //做业务字段校验
        Result validResult = BusinessCfgUtil.validAttr(creditApplyDraftSaveDTO.getCompanyId()
                , businessType.getId()
                , CommonEnum.WhetherEnum.YES.getStatus()
                , creditApplyDraftSaveDTO.getTableName()
                , creditApplyDraftSaveDTO.getValue()
                ,CommonEnum.QueryType.CREATE.getStatus());
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
            return validResult.toJSON();
        return creditApplyFeignService.saveDraft(creditApplyDraftSaveDTO);
    }

    @Override
    public String commit(CreditApplyCommitDTO creditApplyCommitDTO) {
        return creditApplyFeignService.commit(creditApplyCommitDTO);
    }

    //我的申请列表
    @Override
    public String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO) {
        return creditApplyFeignService.myApplyList(creditApplyMyListQueryDTO);
    }

    @Override
    public Result<Object> getDetail(CreditApplyQueryDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");

        List<Map<String, Object>> fromAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, dto.getQueryType());

        //获取详情附件配置
        List<BusinessFileCfg> formFileCfgList =
                BusinessCfgUtil.getBusinessFileCfgList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());

        Result res = creditApplyFeignService.getDetail(dto);
        if(res.getCode() == Result.RESULT_CODE_FAILURE) return Result.failure("查询授信申请详情失败");

        List<BusinessAttrVal> attrValList = BusinessCfgUtil.getBusinessAttrValList(dto.getCompanyId(),businessType.getId(),null,CommonEnum.StatusEnnum.status1.getStatus());

        Map<String, Object> data = new HashMap<>();
        data.put("attrValList", attrValList);
        data.put("fromAttrList", fromAttrList);
        data.put("creditApplyDetails", res.getData());
        data.put("formFileCfgList", formFileCfgList);
        return Result.success(data);
    }

    @Override
    public Result getWorkFlowFileCfg(CreditApplyQueryDTO dto) {
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息");

        List<Role> roleList = MySubjectUtil.getRoleList();

        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow businessFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getFlowCode(), businessFlowList);

        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),businessType.getId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+businessFlow.getFlowName()+"】获取后续流程失败");

        //获取当前流程的所有可能扭转流程的配置
        List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode(dto.getFlowCode(), workFlowCfgList);
        if(trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()) return  Result.failure("流程【"+businessFlow.getFlowName()+"】获取扭转配置失败");

        //排除登录用户没有权限的流程扭转配置
        trailingWorkFlowCfg = BusinessCfgUtil.powerFilterWorkFlowCfg(trailingWorkFlowCfg, user.getId(), roleList);

        Map<String, Object> data = new HashMap<>();
        data.put("trailingWorkFlowCfg",trailingWorkFlowCfg);   //获取流程扭转文件配置

        Result<List<File>> result =
                fileFeignService.findSelective(
                        new FileDTO().setCompanyId(dto.getCompanyId())
                                .setBusinessTypeId(businessType.getId())
                                .setBusinessId(dto.getCreditApplyId()));
        List<File> fileList = result.getData();
        data.put("fileList", fileList == null ? new ArrayList<>():fileList);

        data.putAll(JsonUtil.beanToMap(dto));
        return Result.success(data);
    }

    @Override
    public Result appAuditCommit(CreditAuditCommitDTO dto) {
        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow beforeFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getBeforeFlow(), businessFlowList);
        BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);

        //获取审批流程相关配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),dto.getBusinessTypeId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+beforeFlow.getFlowName()+"】获取后续流程失败");

        BusinessWorkFlowExtendVO workFlowExtendVO = BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getBeforeFlow(),dto.getAfterFlow(),workFlowCfgList);
        if(workFlowExtendVO == null) return Result.failure("流程【"+beforeFlow.getFlowName()+"】未配置指向【"+afterFlow.getFlowName()+"】流程");

        //校验文件项
        List<File> fileList = fileTransfer.toFileList(dto.getFileList());
        Result valid = BusinessCfgUtil.validFiles(fileList,workFlowExtendVO.getWorkFlowFileVOList());
        if(valid.getCode() != Result.RESULT_CODE_SUCCESS) return valid;

        //将审核表单数据封装
        Map<String,List<AuditFormDTO>> formMap = dto.getFormDataList().stream().collect(Collectors.groupingBy(form -> {
            String tableName = form.getColumnName().split("_")[0];
            return tableName;
        }));

        //校验审批字段
        Set<String> keys = formMap.keySet();
        for(String key:keys){
            valid = BusinessCfgUtil.validAttr(key,formMap.get(key),workFlowExtendVO.getBusinessAttrCfgVOList(),CommonEnum.QueryType.AUDIT.getStatus());
            if(valid.getCode() != Result.RESULT_CODE_SUCCESS){
                return valid;
            }
        }

        return creditApplyFeignService.appAuditCommit(dto);
    }
}
