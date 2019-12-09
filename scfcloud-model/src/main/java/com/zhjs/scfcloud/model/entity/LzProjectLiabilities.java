package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-06 16:23
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_liabilities")
public class LzProjectLiabilities {
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
     * 应还借款利息
     * repay_interest
     */
    private BigDecimal repayInterest;

    /**
     * 工程贷剩余本金
     * project_loan_repay_principal
     */
    private BigDecimal projectLoanRepayPrincipal;

    /**
     * 工程贷剩余本金
     * bill_repay_principal
     */
    private BigDecimal billRepayPrincipal;

    /**
     * 经营借款剩余本金
     * deal_in_loan_repay_principal
     */
    private BigDecimal dealInLoanRepayPrincipal;

    /**
     * 应付材料费余额
     * repay_materials_fee
     */
    private BigDecimal repayMaterialsFee;

    /**
     * 已支付管理费、税金
     * pd_paid_manage_fee
     */
    private BigDecimal pdPaidManageFee;

    /**
     * 已支付人工费
     * pd_paid_labor_fee
     */
    private BigDecimal pdPaidLaborFee;

    /**
     * 已支付材料费
     * pd_paid_materials_fee
     */
    private BigDecimal pdPaidMaterialsFee;

    /**
     * 已支付项目费用
     * pd_paid_project_fee
     */
    private BigDecimal pdPaidProjectFee;

    /**
     * 项目经营剩余
     * project_manage_surplus
     */
    private BigDecimal projectManageSurplus;

    /**
     * 统计日期
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
