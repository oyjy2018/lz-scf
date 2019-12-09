package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.CompanyJdVerified;

public interface CompanyJdVerifiedService extends IService<CompanyJdVerified> {
    /**
     * 京东账户注册
     * @param companyId
     * @param mobile
     * @return
     */
    void companyRegister(Long companyId,String mobile);

    /**
     * 获取京东实名认证链接
     * @param companyId
     * @return
     */
    String selectRealUrl(Long companyId);
}
