package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;

/**
 * 商票订单相关
 */
public interface TicketOrderService {
    String getAllOrderList(BusinessTicketOrderAllListDTO dto);

    String reStartTicketStatusQuery(Long id);

    String findOrderDetails(OrderBaseDTO dto);

    Result inquireDetail(Long inquireId);

    Result quotationDetail(Long quotationId);
}
