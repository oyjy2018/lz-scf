package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author weijie.chen
 */
@Data
public class CompanyBankSaveVO{
    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 银行编码
     * bank_code
     */
    @NotNull(message = "银行编码不能为空")
    private String bankCode;

    /**
     * 银行名称
     * bank_name
     */
    @NotNull(message = "银行名称不能为空")
    private String bankName;

    /**
     * 银行支行银联号
     */
    @NotNull(message = "银行支行银联号不能为空")
    private String bankCnaps;

    /**
     * 支行银行全称
     * bank_child_name
     */
    @NotNull(message = "支行银行全称不能为空")
    private String bankChildName;

    /**
     * 银行账号
     * bank_account_no
     */
    @NotNull(message = "银行账号不能为空")
    private String bankAccountNo;

    /**
     * 账号名称
     * bank_account_name
     */
    @NotNull(message = "账号名称不能为空")
    private String bankAccountName;

    /**
     * 账号类型(1:收票账户 2:收款账户)
     * account_type
     */
    @NotNull(message = "账号类型不能为空")
    private String accountType;

    /**
     * 省ID
     * province_id
     */
    @NotNull(message = "省ID不能为空")
    private String provinceId;

    /**
     * 省名称
     * province_name
     */
    @NotNull(message = "省名称不能为空")
    private String provinceName;

    /**
     * 城市ID
     * city_id
     */
    @NotNull(message = "城市ID不能为空")
    private String cityId;

    /**
     * 城市名称
     * city_name
     */
    @NotNull(message = "城市名称不能为空")
    private String cityName;

}
