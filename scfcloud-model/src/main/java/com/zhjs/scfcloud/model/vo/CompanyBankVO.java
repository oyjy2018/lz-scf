package com.zhjs.scfcloud.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author weijie.chen
 */
@Data
public class CompanyBankVO {

    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 银行编码
     * bank_code
     */
    private String bankCode;

    /**
     * 银行名称
     * bank_name
     */
    private String bankName;

    /**
     * 支行银行全称
     * bank_child_name
     */
    private String bankChildName;

    /**
     * 银行账号
     * bank_account_no
     */
    private String bankAccountNo;

    /**
     * 账号名称
     * bank_account_name
     */
    private String bankAccountName;

    /**
     * 账号类型(1:收票账户 2:收款账户)
     * account_type
     */
    private String accountType;

    /**
     * 账户是否授权ECDS状态（0：未认证；1：已认证）PS：收票账户用
     * ecds_type
     */
    private Integer ecdsType;

    /**
     * 是否为收票默认账户(0:否 1:是)
     * is_receipt_default
     */
    private Integer isReceiptDefault;

    /**
     * 是否为收款默认账户(0:否 1:是)
     * is_repay_default
     */
    private Integer isRepayDefault;

    /**
     * 省ID
     * province_id
     */
    private String provinceId;

    /**
     * 省名称
     * province_name
     */
    private String provinceName;

    /**
     * 城市ID
     * city_id
     */
    private String cityId;

    /**
     * 城市名称
     * city_name
     */
    private String cityName;

    /**
     * 账户状态(1:认证中 2:认证成功 3:认证失败 4:认证过期)
     * account_status
     */
    private Integer accountStatus;


    /**
     * 打款认证状态(由京东定义)
     * sub_status
     */
    private String subStatus;

    /**
     * 打款成功时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paySuccessTime;

}
