package com.zhjs.scfcloud.ticket.controller.jd.handler;

import com.jd.jr.jropen.sdk.model.message.AsyncNotifyMerRechargeMessageBody;
import org.springframework.stereotype.Component;


/**
 * 余额充值
 */
@Component
public class BalanceRechargeNotifyHandler implements NotifyHandler<AsyncNotifyMerRechargeMessageBody> {

    @Override
    public void handle(AsyncNotifyMerRechargeMessageBody body) {
        // TODO 业务逻辑处理
    }
}
