package com.zhjs.scfcloud.ticket.service;

import com.jd.jr.cbp.sdk.entity.notify.request.NotifyEcdsResult;
import com.jd.jr.cbp.sdk.entity.notify.request.OrderStatusMessageBody;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.QueryOrderListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.RevokeOrderDTO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderDetailsVO;
import com.zhjs.scfcloud.model.vo.businessTicket.OrderListVO;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/7/17 14:34
 */
public interface BusinessTicketOrderService {

    /**
     * 查询订单列表
     * @param dto
     * @return
     */
    public Result findOrderList(QueryOrderListDTO dto);

    /**
     * 查询订单详情
     * @param dto
     * @return
     */
    OrderDetailsVO findOrderDetails(OrderBaseDTO dto);

    /**
     * 支付订单
     * @param dto
     * @return
     */
    Result payOrder(OrderBaseDTO dto);

    /**
     * 撤销订单
     * @param dto
     * @return
     */
    Result revokeOrder(RevokeOrderDTO dto);

    /**
     * 京东回调更新订单状态
     * @param orderStatusMessageBody
     * @return
     */
    Result updateOrderStatus(OrderStatusMessageBody orderStatusMessageBody);

    /**
     * 更新订单票据状态
     * @param ecdsResultMessageBody
     * @return
     */
    Result updateOrderBillStatus(NotifyEcdsResult ecdsResultMessageBody);

    /**
     * 所有订单
     * @param dto
     * @return
     */
    String allOrder(BusinessTicketOrderAllListDTO dto);

    //重新发起轮询
    Result reStartTicketStatusQuery(Long id);
}
