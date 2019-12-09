package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import com.zhjs.scfcloud.model.entity.BusinessFlow;
import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.transfer.FileTransfer;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.service.AuditService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 业务流程审批相关
 */
@Service
public class AuditServiceImpl implements AuditService {
    @Resource
    private FileTransfer fileTransfer;


    @Override
    public Result validParam(CommonAuditCommitDTO dto) {
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
        return Result.success();
    }
}
