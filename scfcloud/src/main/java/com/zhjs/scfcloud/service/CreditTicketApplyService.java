package com.zhjs.scfcloud.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;

/**
 * @author: dailongting
 * @date:2019/6/28 17:04
 */
public interface CreditTicketApplyService {

    /**
     *
     * @param dto
     * @return
     */
    Result findCreditTicketCfg(QueryCreditTicketCfgDTO dto);

    /**
     *
     * @param dto
     * @return
     */
    Result saveDraft(CreditTicketDraftSaveDTO dto);

    Result auditCommit(CommonAuditCommitDTO dto);

    /**
     * 开票申请保存文件（需判断是否需要新增开票申请草稿记录）
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
