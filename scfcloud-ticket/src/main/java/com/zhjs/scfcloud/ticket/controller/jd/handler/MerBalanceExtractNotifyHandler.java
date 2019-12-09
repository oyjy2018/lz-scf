package com.zhjs.scfcloud.ticket.controller.jd.handler;

import com.jd.jr.jropen.sdk.model.message.AsyncNotifyMerBalanceExtractMessageBody;
import org.springframework.stereotype.Component;


/**
 * 余额提现
 */
@Component
public class MerBalanceExtractNotifyHandler implements NotifyHandler<AsyncNotifyMerBalanceExtractMessageBody> {

    @Override
    public void handle(AsyncNotifyMerBalanceExtractMessageBody body) {
        // TODO 业务逻辑处理
    }
}
