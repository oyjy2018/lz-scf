package com.zhjs.scfcloud.ticket.controller.jd;

import com.alibaba.fastjson.JSON;
import com.jd.jr.jropen.sdk.api.AsyncNotifyMerMessageService;
import com.jd.jr.jropen.sdk.api.EnterpriseInfoService;
import com.jd.jr.jropen.sdk.enums.AsyncNotifyMerMsgRespStatus;
import com.jd.jr.jropen.sdk.enums.AsyncNotifyMerMsgType;
import com.jd.jr.jropen.sdk.model.AsyncNotifyMerMessage;
import com.jd.jr.jropen.sdk.model.AsyncNotifyMerMessageHeader;
import com.jd.jr.jropen.sdk.model.CommonRequest;
import com.jd.jr.jropen.sdk.model.CommonRequestHeader;
import com.jd.jr.jropen.sdk.model.enter.EnterRegisterRequestBody;
import com.jd.jr.jropen.sdk.model.message.AsyncNotifyMerResponseMessageBody;
import com.zhjs.scfcloud.ticket.controller.jd.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notify/jd")
public class JdAsyncNotifyController {

    private static final Logger logger = LoggerFactory.getLogger(JdAsyncNotifyController.class);

    @Autowired
    RealNameNotifyHandler realNameNotifyHandler;
    @Autowired
    BalanceRechargeNotifyHandler balanceRechargeNotifyHandler;
    @Autowired
    MerBalanceExtractNotifyHandler merBalanceExtractNotifyHandler;
    @Autowired
    RealNameApplyResultNotifyHandler realNameApplyResultNotifyHandler;

    @RequestMapping(value = "/asyncnotify", method = RequestMethod.POST)
    @ResponseBody
    public Object asyncNotify(@RequestBody NotifyObj notifyObj) {
        AsyncNotifyMerMessage merMessage = null;
        Object obj = null;
        try {
            String asyncNotify = JSON.toJSONString(notifyObj);
            logger.debug("asyncNotify：" + asyncNotify);
            merMessage = AsyncNotifyMerMessageService.parse(asyncNotify);

            NotifyHandler handler = handlerMessage(merMessage);


            if (handler != null) {
                handler.handle(merMessage.getMessageBody());
                AsyncNotifyMerResponseMessageBody body = new AsyncNotifyMerResponseMessageBody();
                body.setRespStatus(AsyncNotifyMerMsgRespStatus.SUCC.getCode());
                AsyncNotifyMerMessage<AsyncNotifyMerResponseMessageBody> resmessage = new AsyncNotifyMerMessage<AsyncNotifyMerResponseMessageBody>();
                resmessage.setMessageBody(body);
                resmessage.setMessageHeader(merMessage.getMessageHeader());
                String responseStr = AsyncNotifyMerMessageService.generateResponseMsg(resmessage);
                obj = JSON.parseObject(responseStr);
            } else {
                logger.debug("此消息未处理");
            }
            return obj;
        } catch (Exception e) {
            logger.error("解析京东通知消息失败：", e);
            if (merMessage != null) {
                AsyncNotifyMerResponseMessageBody body = new AsyncNotifyMerResponseMessageBody();
                body.setRespStatus(AsyncNotifyMerMsgRespStatus.FAIL.getCode());
                AsyncNotifyMerMessage<AsyncNotifyMerResponseMessageBody> resmessage = new AsyncNotifyMerMessage<AsyncNotifyMerResponseMessageBody>();
                resmessage.setMessageBody(body);
                resmessage.setMessageHeader(merMessage.getMessageHeader());
                String responseStr = AsyncNotifyMerMessageService.generateResponseMsg(resmessage);
                obj = JSON.parseObject(responseStr);
            }
            return obj;
        }
    }

    public void test(){
        EnterpriseInfoService enterpriseInfoService = new EnterpriseInfoService();
        CommonRequest<EnterRegisterRequestBody> commonRequest = new CommonRequest<>();
        commonRequest.setRequestHeader(new CommonRequestHeader());
        enterpriseInfoService.register(commonRequest);
    }

    private NotifyHandler handlerMessage(AsyncNotifyMerMessage merMessage) {
        AsyncNotifyMerMessageHeader header = merMessage.getMessageHeader();
        AsyncNotifyMerMsgType type = AsyncNotifyMerMsgType.getEnumByType(header.getMessageType());
        switch (type) {
            case MER_ENTER_REAL_NAME_STATUS:
                return realNameNotifyHandler;
            case MER_BALANCE_RECHARGE:
                return balanceRechargeNotifyHandler;
            case MER_BALANCE_EXTRACT:
                return merBalanceExtractNotifyHandler;
            case MER_ENTER_REAL_NAME_RESULT:
                return realNameApplyResultNotifyHandler;
        }
        return null;
    }

}
