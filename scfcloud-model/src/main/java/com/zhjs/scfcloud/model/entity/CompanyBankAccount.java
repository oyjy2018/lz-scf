package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author:dailongting
 * @date:2019-09-29 17:52
 */
@Data
@Accessors(chain = true)
@TableName("scf_company_bank_account")
public class CompanyBankAccount {
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
     * 银行支行银联号
     * bank_cnaps
     */
    private String bankCnaps;

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
     * 京东银行信息绑定ID
     * bind_id
     */
    private String bindId;

    /**
     * 代付单Id
     * pay_order_id
     */
    private Long payOrderId;

    /**
     * 11:待发起代付
12:待打款
13:打款成功
21:代付失败
22:打款失败
23:金额确认失败
24:打款申请失败次数超限
25:金额验证失败次数超限
30:退票
40:金额确认成功
50:认证过期
     * sub_status
     */
    private String subStatus;

    /**
     * 打款成功时间
     * pay_success_time
     */
    private LocalDateTime paySuccessTime;

    /**
     * 创建人ID
     * create_by_id
     */
    private Long createById;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;

    /**
     * 修改人ID
     * update_by_id
     */
    private Long updateById;

    /**
     * 修改时间
     * update_time
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志:0未删除;1已删除
     * is_delete
     */
    private Integer isDelete;
}
