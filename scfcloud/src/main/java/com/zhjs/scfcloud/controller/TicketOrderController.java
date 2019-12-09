package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.TicketOrderService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 商票订单相关
 */
@RestController
@RequestMapping("/ticketOrder/")
public class TicketOrderController {
    private Logger logger = LoggerFactory.getLogger(TicketOrderController.class);

    @Autowired
    private TicketOrderService ticketOrderService;

    //获取所有商票订单
    @PostMapping("getAllOrderList")
    @RequiresPermissions("common:order:list")
    public String getAllOrderList(@RequestBody BusinessTicketOrderAllListDTO dto){
        logger.info("subject:{},dto:{}","获取所有商票订单",dto);
        return ticketOrderService.getAllOrderList(dto);
    }

    //重新发起商票状态轮询
    @PostMapping("reStartTicketStatusQuery")
    @RequiresPermissions("common:order:reStart")
    public String reStartTicketStatusQuery(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","重新发起商票状态轮询",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("订单id为空").toJSON();
        return ticketOrderService.reStartTicketStatusQuery(dto.getId());
    }

    /**
     * 查询订单详情
     * @param dto
     * @param result
     * @return
     */
    @PostMapping("/findOrderDetails")
    public String findOrderDetails(@RequestBody @Valid OrderBaseDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","查询订单详情", JsonUtil.toJSON(dto));
        if (result.hasErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage()).toJSON();
        }
        return ticketOrderService.findOrderDetails(dto);
    }

    //询价详情
    @PostMapping("/inquireDetail")
    public Result inquireDetail(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","询价详情",dto.toString());

        if (StringUtil.isEmpty(dto.getId())) return Result.failure("询价单id为空");

        return ticketOrderService.inquireDetail(dto.getId());
    }

    @ApiOperation("报价单详情")
    @GetMapping("/quotationDetail/{quotationId}")
    public Result detail(@PathVariable Long quotationId){
        logger.info("subject:{},quotationId:{}","报价单详情",quotationId.toString());

        return ticketOrderService.quotationDetail(quotationId);
    }
}
