package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.OrderBaseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "scfcloud-system", contextId = "ticketOrder")
public interface TicketOrderFeignService {

    //获取所有商票订单
    @PostMapping("/ticketOrder/getAllOrderList")
    String getAllOrderList(@RequestBody BusinessTicketOrderAllListDTO dto);

    //重新发起商票状态轮询
    @PostMapping("/ticketOrder/reStartTicketStatusQuery")
    String reStartTicketStatusQuery(@RequestParam("id") Long id);

    //查询订单详情
    @PostMapping("/ticketOrder/findOrderDetails")
    String findOrderDetails(@RequestBody OrderBaseDTO dto);

    //查询询价详情
    @PostMapping("/ticketOrder/inquireDetail")
    Result inquireDetail(@RequestParam("inquireId") Long inquireId);

    //查询报价详情
    @PostMapping("/ticketOrder/quotationDetail")
    Result quotationDetail(@RequestParam("quotationId") Long quotationId);
}
