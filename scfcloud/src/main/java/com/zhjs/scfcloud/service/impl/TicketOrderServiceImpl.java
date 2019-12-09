package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.feign.TicketOrderFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.service.TicketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketOrderServiceImpl implements TicketOrderService {

    @Autowired
    private TicketOrderFeignService ticketOrderFeignService;

    //获取所有商票订单
    @Override
    public String getAllOrderList(BusinessTicketOrderAllListDTO dto) {
        return ticketOrderFeignService.getAllOrderList(dto);
    }

    //重新发起商票状态轮询
    @Override
    public String reStartTicketStatusQuery(Long id) {
        return ticketOrderFeignService.reStartTicketStatusQuery(id);
    }

    //查询订单详情
    @Override
    public String findOrderDetails(OrderBaseDTO dto) {
        return ticketOrderFeignService.findOrderDetails(dto);
    }

    @Override
    public Result inquireDetail(Long inquireId) {
        return ticketOrderFeignService.inquireDetail(inquireId);
    }

    @Override
    public Result quotationDetail(Long quotationId) {
        return ticketOrderFeignService.quotationDetail(quotationId);
    }
}
