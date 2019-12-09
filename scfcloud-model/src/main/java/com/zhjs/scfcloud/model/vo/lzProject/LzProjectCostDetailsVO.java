package com.zhjs.scfcloud.model.vo.lzProject;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: dailongting
 * @date:2019/10/31 15:08
 */
@Data
public class LzProjectCostDetailsVO {
    /**
     * 项目ID（项目合同号）
     */
    private String contractNo;

    /**
     * 费用类型
     */
    private String feeType;

    /**
     * 最小控制指标
     */
    private BigDecimal guidepostsMin;

    /**
     * 最大控制指标
     */
    private BigDecimal guidepostsMax;

    /**
     * 控制指标
     */

    private String guidepostsStr;

    /**
     * 主合同金额
     */
    private BigDecimal contractMoney;

    private String contractMoneyStr;

    /**
     * 预算比例
     */
    private BigDecimal budgetRate;

    private String  budgetRateStr;

    /**
     * 预算金额
     */
    private BigDecimal budgetMoney;

    private String budgetMoneyStr;

    /**
     * 冻结金额
     */
    private BigDecimal freezeMoney;

    private String freezeMoneyStr;

    /**
     * 使用金额
     */
    private BigDecimal useMoney;

    private String useMoneyStr;

    /**
     * 预算余额
     */
    private BigDecimal budgetBalance;

    private String budgetBalanceStr;

    public String getGuidepostsStr() {
        return this.guidepostsMin.multiply(BigDecimal.valueOf(100)).setScale(1, BigDecimal.ROUND_HALF_UP)
                + "%" + " - " + this.guidepostsMax.multiply(BigDecimal.valueOf(100)).setScale(1, BigDecimal.ROUND_HALF_UP) +"%";
    }

    public BigDecimal getBudgetMoney() {
        return this.contractMoney.multiply(this.budgetRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getBudgetBalance() {
        return getBudgetMoney().subtract(this.useMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getContractMoneyStr() {
        return getContractMoney() + "";
    }

    public String getBudgetRateStr() {
        return getBudgetRate().multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
    }

    public String getBudgetMoneyStr() {
        return getBudgetMoney() + "";
    }

    public String getFreezeMoneyStr() {
        return getFreezeMoney() + "";
    }

    public String getUseMoneyStr() {
        return getUseMoney() + "";
    }

    public String getBudgetBalanceStr() {
        return getBudgetBalance() + "";
    }
}
