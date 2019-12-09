package com.zhjs.scfcloud.ticket.controller;

import com.jd.jr.cbp.sdk.SdkException;
import com.jd.jr.cbp.sdk.entity.notify.CommonNotify;
import com.jd.jr.cbp.sdk.entity.notify.NotifyHeader;
import com.jd.jr.cbp.sdk.entity.notify.NotifyResponse;
import com.jd.jr.cbp.sdk.entity.notify.request.NotifyAgencyResult;
import com.jd.jr.cbp.sdk.entity.notify.request.NotifyEcdsResult;
import com.jd.jr.cbp.sdk.entity.notify.request.OrderStatusMessageBody;
import com.jd.jr.cbp.sdk.enums.NotifyMessageTypeEnum;
import com.jd.jr.cbp.sdk.enums.NotifyRespStatusEnum;
import com.jd.jr.cbp.sdk.services.notify.NotifyService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.ticket.service.BusinessTicketOrderService;
import com.zhjs.scfcloud.ticket.service.JdCompanyBankService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述 : 接收异步通知Controller
 * @author: dailongting
 * @date:2019/7/11 17:16
 */
@Controller
    @RequestMapping("/platform")
public class PlatformNotifyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotifyService notifyService;
    @Autowired
    private JdCompanyBankService jdCompanyBankService;
    @Autowired
    private BusinessTicketOrderService businessTicketOrderService;

    /**
     * 京东智票统一异步回调接口
     * @param commonNotice
     * @param response
     */
    @RequestMapping(value = "/jdzpNotify", method = {RequestMethod.POST}, produces = "application/json")
    public void notify(@RequestBody CommonNotify commonNotice, HttpServletResponse response) {
        NotifyHeader header = commonNotice.getHeader();
        String messageType = header.getMessageType();
        Result result = Result.success();
        System.out.println(messageType);
        if (messageType.equals(NotifyMessageTypeEnum.ORDER_STATUS_MSG.getCode())) {
            //订单业务消息
            OrderStatusMessageBody orderStatusMessageBody = null;
            try {
                orderStatusMessageBody = notifyService.parse(commonNotice);
                logger.info("处理京东智票异步回调通知:订单业务消息回调:body:{}", JsonUtil.toJSON(orderStatusMessageBody));
                // 业务处理
                result = businessTicketOrderService.updateOrderStatus(orderStatusMessageBody);
            } catch (SdkException e) {
                e.printStackTrace();
                result = Result.failure("处理京东智票异步回调通知异常：更新订单状态失败！请通知我方运营或产品！");
            }
        } else if (messageType.equals(NotifyMessageTypeEnum.QUERY_ECDS_MSG.getCode())) {
            //ECDS业务消息
            NotifyEcdsResult ecdsResultMessageBody = null;
            try {
                ecdsResultMessageBody = notifyService.parse(commonNotice);
                logger.info("处理京东智票异步回调通知:ECDS业务消息回调:body:{}", JsonUtil.toJSON(ecdsResultMessageBody));
                // 业务处理
                result = businessTicketOrderService.updateOrderBillStatus(ecdsResultMessageBody);
            } catch (SdkException e) {
                e.printStackTrace();
                result = Result.failure("处理京东智票异步回调通知异常：更新背书状态失败！请通知我方运营或产品！");
            }
        }else if (messageType.equals(NotifyMessageTypeEnum.AGENCY_RESULT_MSG.getCode())) {
            //小额打款结果通知
            NotifyAgencyResult notifyAgencyResult = null;
            try {
                notifyAgencyResult = notifyService.parse(commonNotice);
                logger.info("处理京东智票异步回调通知:小额打款结果通知回调:body:{}", JsonUtil.toJSON(notifyAgencyResult));
                // 业务处理
                jdCompanyBankService.updateBankStatusByAgency(notifyAgencyResult.getPayOrderId(),notifyAgencyResult.getSubStatus());
            } catch (SdkException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        // 设置响应结果
        NotifyResponse notifyResponse = new NotifyResponse();
        if(result.getCode() == Result.RESULT_CODE_SUCCESS){
            notifyResponse.setRespStatus(NotifyRespStatusEnum.SUCCESS.getCode());
        }else{
            notifyResponse.setRespStatus(NotifyRespStatusEnum.FAIL.getCode());
        }
        notifyResponse.setRespMsg(result.getMessage());
        String resp = notifyService.generateResponse(commonNotice.getHeader(), notifyResponse);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(resp);
        out.close();
    }
}
