package com.zhjs.scfcloud.ticket.controller.jd.handler;

import com.jd.jr.jropen.sdk.model.message.AsyncNotifyEnterRealNameApplyResultMessageBody;
import com.zhjs.scfcloud.ticket.service.CompanyVerifiedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 企业/个体工商户实名认证申请单审核结果通知
 */
@Component
public class RealNameApplyResultNotifyHandler implements NotifyHandler<AsyncNotifyEnterRealNameApplyResultMessageBody> {
    private final static Logger logger = LoggerFactory.getLogger(RealNameApplyResultNotifyHandler.class);

    @Autowired
    private CompanyVerifiedService companyVerifiedService;

    @Override
    public void handle(AsyncNotifyEnterRealNameApplyResultMessageBody body) {
        // TODO 业务逻辑处理
        //保存审核结果
        companyVerifiedService.saveInfoForJdReview(body.getMerCustomerId(),body.getMerchantId(),body.getPartnerApplyId(),body.getAuditStatus(),body.getSubAuditStatus());
    }
}
