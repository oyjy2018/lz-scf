package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.vo.BankDefaultVO;
import com.zhjs.scfcloud.model.vo.CompanySystemVersionNameVO;

import java.util.List;

/**
 * 公司公共服务
 */
public interface CompanyCommonService {
    /**
     * 获取公司成员数
     * @param companyId
     * @param status
     * @return
     */
    Integer userCountByCompanyId(Long companyId,Integer status);

    /**
     * 是否已进行京东企业注册(0:否 1:是)
     * @param companyId
     * @return 0:否 1:是
     */
    Integer isJdRegister(Long companyId);

    /**
     * 是否已进行京东企业认证(0:否 1:是)
     * @param companyId
     * @return 0:否 1:是
     */
    Integer isJdVerified(Long companyId);

    /**
     * 是否已设置收票收款账户(isReceiptAccount-收票  isRepayAccount-收款)
     * @param companyId
     * @return isReceiptAccount 0:否 1:是
     * @return isRepayAccount 0:否 1:是
     */
    BankDefaultVO isDefaultBankAccount(Long companyId);

    /**
     * 是否已进行e签宝企业注册(0:否 1:是)
     * @param companyId
     * @return 0:否 1:是
     */
    Integer esignVerified(Long companyId);

    /**
     * 获取公司选择的系统版本名称（riskSystemVersionName-风控 ticketSystemVersionName-票据）
     * @param companyId
     * @return riskSystemVersionName 风控平台
     * @return ticketSystemVersionName 票据平台
     */
    CompanySystemVersionNameVO selectCompanySystemVersion(Long companyId);

    /**
     * 获取公司选择的系统版本名称（riskSystemVersionName-风控 ticketSystemVersionName-票据）
     * @param systemVersionIds
     * @return riskSystemVersionName 风控平台
     * @return ticketSystemVersionName 票据平台
     */
    CompanySystemVersionNameVO selectCompanySystemVersion(List<Long> systemVersionIds);

    String selectCompanySystemVersionSingleton(Long systemVersionId);

    /**
     * 获取用户加入的有效公司列表
     * @param userId
     * @return
     */
    List<Company> selectCompanyListByUserId(Long userId);
}
