package com.zhjs.scfcloud.ticket.controller.jd.handler;

import com.jd.jr.jropen.sdk.model.message.AsyncNotifyEnterRealNameStatusMessageBody;
import com.zhjs.scfcloud.ticket.service.CompanyVerifiedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 企业/个体工商户实名状态变更异步通知
 */
@Component
public class RealNameNotifyHandler implements NotifyHandler<AsyncNotifyEnterRealNameStatusMessageBody> {
    private final static Logger logger = LoggerFactory.getLogger(RealNameNotifyHandler.class);

    @Autowired
    private CompanyVerifiedService companyVerifiedService;

    @Override
    public void handle(AsyncNotifyEnterRealNameStatusMessageBody body) {
       // TODO 业务逻辑处理

       //更新京东实名认证信息
       if("4".equals(body.getMerRealStatus())){
           companyVerifiedService.saveRealNameResult(body.getMerCustomerId());
       }
    }
}
