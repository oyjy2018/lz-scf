package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import com.zhjs.scfcloud.model.entity.CreditRecord;

import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/6/28 16:45
 */
public interface CreditTicketApplyService {

    /**
     * 查询开票申请详情
     * @param creditTicketApplyId
     * @return
     */
    Map<String, Object> findCreditTicketApplyDetails(Long creditTicketApplyId);

    Result saveDraft(CreditTicketDraftSaveDTO dto);

    Result auditCommit(CommonAuditCommitDTO dto);

    /**
     * 开票申请保存附件
     * @param dto
     * @return
     */
    Result saveFile(CreditTicketFileSaveDTO dto);

    /**
     * 开票申请提交
     * @param dto
     * @return
     */
    Result applyCommit(CreditTicketApplyCommitDTO dto);

    /**
     * 计算项目剩余额度
     * @param recordId
     * @return
     */
    Result getRemainCreditAmount(Long recordId);

    /**
     * 计算项目剩余额度
     * @param record
     * @return
     */
    Result getRemainCreditAmount(CreditRecord record);

    /**
     * 查询审核过程数据
     * @param dto
     * @return
     */
    Result findApprovalCourse(CreditTicketApplyBaseDTO dto);

    /**
     * 查询开票申请详情（新）
     * @param dto
     * @return
     */
    Result getDetails(QueryApplyDetailsDTO dto);

    /**
     * 审批完成列表
     * @param dto
     * @return
     */
    Result getAuditFinishList(CreditUseApplyAuditFinishListQueryDTO dto);
}
