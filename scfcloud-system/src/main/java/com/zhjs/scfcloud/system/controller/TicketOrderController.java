package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.system.service.TicketOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商票订单相关
 */
@RestController
@RequestMapping("/ticketOrder/")
public class TicketOrderController {
    private Logger logger = LoggerFactory.getLogger(TicketOrderController.class);

    @Resource
    private TicketOrderService ticketOrderService;

    //获取所有商票订单
    @PostMapping("getAllOrderList")
    public String getAllOrderList(@RequestBody BusinessTicketOrderAllListDTO dto){
        logger.info("subject:{},dto:{}","获取所有商票订单",dto);
        return ticketOrderService.getAllOrderList(dto);
    }
    //重新发起商票状态轮询
    @PostMapping("reStartTicketStatusQuery")
    public String reStartTicketStatusQuery(@RequestParam("id") Long id) {
        logger.info("subject:{},id:{}","重新发起商票状态轮询",id);
        return ticketOrderService.reStartTicketStatusQuery(id);
    }

    //查询订单详情
    @PostMapping("findOrderDetails")
    public String findOrderDetails(@RequestBody OrderBaseDTO dto){
        logger.info("subject:{},dto:{}","查询订单详情",dto);
        return ticketOrderService.findOrderDetails(dto);
    }

    //查询询价详情
    @PostMapping("inquireDetail")
    public Result inquireDetail(@RequestParam("inquireId") Long inquireId) {
        logger.info("subject:{},inquireId:{}","查询询价详情",inquireId);
        return ticketOrderService.inquireDetail(inquireId);
    }

    //查询报价详情
    @PostMapping("quotationDetail")
    public Result quotationDetail(@RequestParam("quotationId") Long quotationId) {
        logger.info("subject:{},quotationId:{}","查询报价详情",quotationId);
        return ticketOrderService.quotationDetail(quotationId);
    }
}
