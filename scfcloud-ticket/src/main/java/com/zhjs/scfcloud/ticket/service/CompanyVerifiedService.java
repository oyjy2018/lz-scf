package com.zhjs.scfcloud.ticket.service;

/**
 * 公司实名相关服务
 * @author weijie.chen
 */
public interface CompanyVerifiedService {

    /**
     * 保存实名审核结果
     * @param merCustomerId(商户平台用户的唯一标识，公司ID)
     * @param merchantId(开放平台商户号)
     * @param partnerApplyId(合作商户实名认证申请单号)
     * @param auditStatus(申请单审核状态)
     * @param subAuditStatus
     */
    public void saveInfoForJdReview(String merCustomerId,String merchantId,String partnerApplyId,String auditStatus,String subAuditStatus);

    /**
     * 更新实名信息
     * @param merCustomerId
     */
    public void saveRealNameResult(String merCustomerId);
}
