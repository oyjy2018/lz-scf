package com.zhjs.scfcloud.ticket.service;

/**
 * 公司银行卡service-京东异步通知调用
 */
public interface JdCompanyBankService {
    /**
     * 小额打款异步通知-修改打款认证状态
     */
    void updateBankStatusByAgency(Long payOrderId,String subStatus);
}
