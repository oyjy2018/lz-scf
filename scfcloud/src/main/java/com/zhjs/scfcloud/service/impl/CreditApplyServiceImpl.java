package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.feign.CreditApplyFeignService;
import com.zhjs.scfcloud.feign.RoleFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.enums.OperatObjectEnum;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.service.CreditApplyService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.ShiroSessionUtil;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 授信申请
 */
@Service
public class CreditApplyServiceImpl implements CreditApplyService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditApplyFeignService creditApplyFeignService;
    @Autowired
    private RoleFeignService roleFeignService;
    @Resource
    private BusinessCfgUtil businessCfgUtil;

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

    @Override
    public String saveFile(CreditApplyFileSaveDTO dto) {
        //校验文件
        List<File> fileList = dto.getFileList();
        if (fileList == null || fileList.isEmpty())
            return Result.failure("资料不能为空").toJSON();
        //做校验
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据").toJSON();
        }
        BusinessWorkFlowExtendVO workFlowExtendVO = BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getWorkFlowId(), dto.getCompanyId(), businessType.getId());
        BusinessWorkFlowFileVO workFlowFileVO =
                workFlowExtendVO.getWorkFlowFileVOList()
                        .stream()
                        .filter( vo -> vo.getFileCode().equals(fileList.get(0).getFileCode()))
                        .findAny().orElse(null);
        Result validResult = BusinessCfgUtil.validFile(fileList,workFlowFileVO);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
            return validResult.toJSON();

        return creditApplyFeignService.saveFile(dto);
    }

    //获取我的申请列表
    @Override
    public String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO) {
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyMyListQueryDTO.setCompanyId(userInfoVO.getCompanyId());
        creditApplyMyListQueryDTO.setUserId(userInfoVO.getId());

        //需要查询待办  查询出所有当前用户有权限的处理的流程
        if ("true".equals(creditApplyMyListQueryDTO.getIsWait())) {
            creditApplyMyListQueryDTO.setRoleIds(userInfoVO.getRoleList().stream().map(role -> role.getId().toString()).collect(Collectors.joining(",")));
        }
        creditApplyMyListQueryDTO.setClient("web");

        return creditApplyFeignService.myApplyList(creditApplyMyListQueryDTO);
    }


    /**
     * 授信审批提交
     * @param dto 入参
     * @return
     */
    @Override
    public String auditCommit(CommonAuditCommitDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据").toJSON();
        }
        // 文件校验
        List<File> files = new ArrayList<>();
        for(FileDTO item: dto.getFileList()){
            File file = new File();
            file.setId(item.getId());
            file.setFileCode(item.getFileCode());
            file.setFileSize(item.getFileSize());
            file.setStatus(item.getStatus());
            file.setSuffix(item.getSuffix());
            file.setFileUrl(item.getFileUrl());
            file.setFileType(item.getFileType());
            file.setBusinessId(item.getBusinessId());
            file.setCompanyId(item.getCompanyId());
            file.setBusinessTypeId(item.getBusinessTypeId());
            file.setOriginalFileName(item.getOriginalFileName());
            file.setNewFileName(item.getNewFileName());
            files.add(file);
        }
        List<BusinessWorkFlowExtendVO> workFlowExtendList = BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(), businessType.getId());
        BusinessWorkFlowExtendVO businessWorkFlowExtend = BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getBeforeFlow(), dto.getAfterFlow(), workFlowExtendList);
        Result result = BusinessCfgUtil.validFiles(files, businessWorkFlowExtend.getWorkFlowFileVOList());
        if(result.getCode() != Result.RESULT_CODE_SUCCESS){
            return result.toJSON();
        }

        //如果当前流程有审批字段，则进行校验
        if(businessWorkFlowExtend.getBusinessAttrCfgVOList() != null && !businessWorkFlowExtend.getBusinessAttrCfgVOList().isEmpty()){
            Map<String, List<BusinessAttrCfgVO>> attrCfgVoMap =
                    businessWorkFlowExtend.getBusinessAttrCfgVOList().stream().collect(Collectors.groupingBy(vo -> vo.getColumnName().split("_")[0]));
            Set<String> keys = attrCfgVoMap.keySet();
            for(String key:keys){
                result = BusinessCfgUtil.validAttr(key,dto.getFormDataList(),attrCfgVoMap.get(key), CommonEnum.QueryType.AUDIT.getStatus());
                if(result.getCode() != Result.RESULT_CODE_SUCCESS){
                    return result.toJSON();
                }
            }

            //剔除掉页面提交不需要保存的字段
            BusinessCfgUtil.rejectUselessAttr(dto.getFormDataList(),businessWorkFlowExtend.getBusinessAttrCfgVOList());
        }

        Result<User> resObj  = creditApplyFeignService.auditCommit(dto);
        //审批通过成功，如果是授信完成，会将授信申请所填身份证更新掉登录用户的身份证（至于为什么？问产品），并且将身份证同步到shiro缓存
        if(resObj.getCode() == Result.RESULT_CODE_SUCCESS){

            // 获取流程list
            List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(), businessType.getId(), null);
            // 获取当前流程
            BusinessFlow businessFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getAfterFlow(), businessFlowList);
            // 判断当前流程是否是授信完成
            if(businessFlow.getFlowType() == 10){
                if(resObj.getData() == null){
                    logger.error("授信审批成功！授信完成，将授信人身份证同步到登录账户失败：获取授信人用户信息失败！");
                }

                User user = resObj.getData();
                logger.info("授信审批成功！授信完成，将授信人身份证同步到登录账户：CreditApplyId={}，userId={}",dto.getBusinessId(),user.getId());
                try{
                    ShiroSessionUtil.updateLoginUserIdCardByUserId(user.getId(),user.getIdCard());
                }catch (Exception e){
                    logger.error("授信审批成功！授信完成，将授信人身份证同步到登录账户异常：CreditApplyId={}，loginUser={}；错误信息：{}",dto.getBusinessId(),user.getId(),e.getMessage());
                }
            }

        }

        return resObj.toJSON();
    }

    //获取所有申请列表
    @Override
    public String allApplyList(CreditApplyAllListQueryDTO creditApplyAllListQueryDTO) {
        return creditApplyFeignService.allApplyList(creditApplyAllListQueryDTO);
    }

    /**
     * 审核日志
     * @param auditLogListDTO
     * @return
     */
    @Override
    public String findAuditLogList(AuditLogListDTO auditLogListDTO) {
        return creditApplyFeignService.findAuditLogList(auditLogListDTO);
    }

    @Override
    public Result<Map<String, Object>> getTrailingWorkFlowCfg(CreditApplyQueryDTO dto) {
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息");

        //查询授信申请详情信息
        Result<Map<String, Object>> res = creditApplyFeignService.findCreditApplyDetails(dto);
        if(res.getCode() == Result.RESULT_CODE_FAILURE) return Result.failure("查询授信申请详情失败");

        Map<String, Object> creditApplyDetails = res.getData();
        Long companyId = Long.valueOf(creditApplyDetails.get("creditApply_companyId").toString());
        Long businessTypeId = Long.valueOf(creditApplyDetails.get("creditApply_businessTypeId").toString());
        BusinessType businessType = BusinessCfgUtil.getBusinessType(companyId, businessTypeId);
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }

        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow businessFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getFlowCode(), businessFlowList);

        //获取流程扭转配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),businessType.getId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+businessFlow.getFlowName()+"】获取后续流程失败");
        //获取当前流程的所有可能扭转流程的配置
        List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode(dto.getFlowCode(), workFlowCfgList);
        if(trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()) return  Result.failure("流程【"+businessFlow.getFlowName()+"】获取扭转配置失败");

        //排除登录用户没有权限的流程扭转配置
        trailingWorkFlowCfg = BusinessCfgUtil.powerFilterWorkFlowCfg(trailingWorkFlowCfg, user.getId(), user.getRoleList());
        //排序 （下一个主流程 要排在首位，“已拒绝”和“当前环节”、“前一环节”的状态不能排在首位）
        trailingWorkFlowCfg = BusinessCfgUtil.sortWorkFlowForAudit(trailingWorkFlowCfg,businessType.getId(),dto.getFlowCode());

        //获取详情表单属性配置
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");
        //将表单属性配置封装
        List<Map<String, Object>> formAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, CommonEnum.QueryType.VIEW.getStatus());

        //获取详情附件配置
        List<BusinessFileCfg> formFileCfgList =
                BusinessCfgUtil.getBusinessFileCfgList(dto.getCompanyId(),businessType.getId(),CommonEnum.StatusEnnum.status1.getStatus());

        //加工授信项目字段
        BusinessCfgUtil.disposeItemCfg(trailingWorkFlowCfg,res.getData());

        List<BusinessAttrVal> attrValList = BusinessCfgUtil.getBusinessAttrValList(dto.getCompanyId(),businessType.getId(),null,CommonEnum.StatusEnnum.status1.getStatus());

        Map<String, Object> data = new HashMap<>();
        data.put("trailingWorkFlowCfg", trailingWorkFlowCfg);
        data.put("formAttrList", formAttrList);
        data.put("creditApplyDetails", creditApplyDetails);
        data.put("attrValList", attrValList);
        //过滤没权限的附件项
        Result<List<String>> functions = roleFeignService.findRoleFilePermissions(MySubjectUtil.getUser().getId());
        List<BusinessFileCfg> filterList = formFileCfgList.stream().filter(businessFileCfg -> functions.getData().contains(businessFileCfg.getFileCode())).collect(Collectors.toList());
        data.put("formFileCfgList", filterList);
        return Result.success(data);
    }

    @Override
    public String findOperateLogList(OperateLogDTO dto) {
        dto.setOperatObject(OperatObjectEnum.credit.getValue());
        return creditApplyFeignService.findOperateLogList(dto);
    }

    @Override
    public Result<Object> getDetail(CreditApplyQueryDTO dto) {
        //查询授信申请详情信息
        Result<Map<String, Object>> res = creditApplyFeignService.findCreditApplyDetails(dto);
        if(res.getCode() == Result.RESULT_CODE_FAILURE) return Result.failure("查询授信申请详情失败");

        Map<String, Object> creditApplyDetails = res.getData();
        Long companyId = Long.valueOf(creditApplyDetails.get("creditApply_companyId").toString());
        Long businessTypeId = Long.valueOf(creditApplyDetails.get("creditApply_businessTypeId").toString());
        BusinessType businessType = BusinessCfgUtil.getBusinessType(companyId, businessTypeId);
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }

        //获取详情表单属性配置
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(companyId,businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");

        //将表单属性配置封装
        List<Map<String, Object>> formAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, CommonEnum.QueryType.VIEW.getStatus());

        //获取详情附件配置
        List<BusinessFileCfg> formFileCfgList =
                BusinessCfgUtil.getBusinessFileCfgList(companyId,businessType.getId(),CommonEnum.StatusEnnum.status1.getStatus());

        List<BusinessAttrVal> attrValList = BusinessCfgUtil.getBusinessAttrValList(companyId,businessType.getId(),null,CommonEnum.StatusEnnum.status1.getStatus());

        Map<String, Object> data = new HashMap<>();
        data.put("creditApplyDetails", res.getData());
        data.put("formAttrList", formAttrList);
        //过滤没权限的附件项
        Result<List<String>> functions = roleFeignService.findRoleFilePermissions(MySubjectUtil.getUser().getId());
        List<BusinessFileCfg> filterList = formFileCfgList.stream().filter(businessFileCfg -> functions.getData().contains(businessFileCfg.getFileCode())).collect(Collectors.toList());
        data.put("formFileCfgList", filterList);
        data.put("attrValList", attrValList);
        return Result.success(data);
    }

    @Override
    public String auditList(CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO) {
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息").toJSON();
        //需要查询待办  查询出所有当前用户有权限的处理的流程
        creditApplyAuditListQueryDTO.setRoleIds(userInfoVO.getRoleList().stream().map(role -> role.getId().toString()).collect(Collectors.joining(",")));

        creditApplyAuditListQueryDTO.setUserId(userInfoVO.getId());
        creditApplyAuditListQueryDTO.setCompanyId(userInfoVO.getCompanyId());
        creditApplyAuditListQueryDTO.setPermissionType(userInfoVO.getPermissionType());
        creditApplyAuditListQueryDTO.setPermissionOrgIds(userInfoVO.getPermissionOrgIds());

        return creditApplyFeignService.auditList(creditApplyAuditListQueryDTO);
    }

    //删除项目
    @Override
    public String deleteCreditItem(CreditItemDeleteDTO creditItemDeleteDTO) {
        return creditApplyFeignService.deleteCreditItem(creditItemDeleteDTO);
    }

    @Override
    public Result findApprovalCourse(CreditApplyBaseDTO dto) {
        return creditApplyFeignService.findApprovalCourse(dto);
    }

    @Override
    public Result getDetails(QueryApplyDetailsDTO dto) {
        return creditApplyFeignService.getDetails(dto);
    }


}
