package com.zhjs.scfcloud.app.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketApplyCommitDTO;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketDraftSaveDTO;
import com.zhjs.scfcloud.model.dto.ticket.CreditTicketFileSaveDTO;
import com.zhjs.scfcloud.model.dto.ticket.QueryCreditTicketCfgDTO;

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
}
