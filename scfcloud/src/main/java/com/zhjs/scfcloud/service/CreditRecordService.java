package com.zhjs.scfcloud.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ticket.QueryCreditTicketCfgDTO;

/**
 * @author: dailongting
 * @date:2019/6/27 17:08
 */
public interface CreditRecordService {

    /**
     * 根据授信记录ID查询
     * @param creditRecordId
     * @return
     */
    Result findPersonalCredit(Long creditRecordId);

}
