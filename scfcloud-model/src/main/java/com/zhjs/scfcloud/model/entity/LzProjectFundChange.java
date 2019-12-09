package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-04 15:56
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_fund_change")
public class LzProjectFundChange {
    /**
     *
     * id
     */
    private Long id;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 累计回款
     * total_gather
     */
    private BigDecimal totalGather;

    /**
     * 累计借款
     * total_loan
     */
    private BigDecimal totalLoan;

    /**
     * 累计保证金
     * total_margins_and_pledge
     */
    private BigDecimal totalMarginsAndPledge;

    /**
     * 累计余额
     * total_balance
     */
    private BigDecimal totalBalance;

    /**
     * 累计付款
     * total_payment
     */
    private BigDecimal totalPayment;

    /**
     * 应收票据
     * bill_receivable
     */
    private BigDecimal billReceivable;

    /**
     * 发票余额
     * invoice_balance
     */
    private BigDecimal invoiceBalance;

    /**
     * 材料预付款
     * materials_advance
     */
    private BigDecimal materialsAdvance;

    /**
     * 材料存货
     * material_inventory
     */
    private BigDecimal materialInventory;

    /**
     * 统计截止时间
     * expiration_date
     */
    private Date expirationDate;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

    /**
     *
     * create_by
     */
    private Long createBy;

    /**
     *
     * create_time
     */
    private Date createTime;

    /**
     *
     * update_by
     */
    private Long updateBy;

    /**
     *
     * update_time
     */
    private Date updateTime;
}
