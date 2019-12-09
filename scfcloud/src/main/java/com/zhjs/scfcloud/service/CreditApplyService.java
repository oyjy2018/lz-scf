package com.zhjs.scfcloud.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 授信申请处理类
 * @author: dailongting
 * @date:2019/6/13 10:39
 */
public interface CreditApplyService {


    String saveDraft(CreditApplyDraftSaveDTO creditApplyDraftSaveDTO);

    String commit(CreditApplyCommitDTO creditApplyCommitDTO);

    String saveFile(CreditApplyFileSaveDTO creditApplyFileSaveDTO);

    String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    /**
     * 授信审批提交
     * @param dto 入参
     * @return
     */
    String auditCommit(CommonAuditCommitDTO dto);

    //所有申请
    String allApplyList(CreditApplyAllListQueryDTO creditApplyAllListQueryDTO);

    /**
     * 获取后续流程配置
     * @param dto
     * @return
     */
    Result getTrailingWorkFlowCfg(CreditApplyQueryDTO dto);

    String findAuditLogList(AuditLogListDTO auditLogListDTO);

    String auditList(CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO);

    String findOperateLogList(OperateLogDTO dto);

    Result<Object> getDetail(CreditApplyQueryDTO dto);

    String deleteCreditItem(CreditItemDeleteDTO creditItemDeleteDTO);

    /**
     * 查询审核过程（最新）
     * @param dto
     * @return
     */
    Result findApprovalCourse(@RequestBody CreditApplyBaseDTO dto);

    /**
     * 查询授信申请详情（最新）
     * @param dto
     * @return
     */
    Result getDetails(@RequestBody QueryApplyDetailsDTO dto);
}
