package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-22 17:40
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_audit_data")
public class CreditAuditDataWithBLOBs extends CreditAuditData {
    /**
     * 客户经理初审-审批分析
     * cust_manager_first_audit
     */
    private String custManagerFirstAudit;

    /**
     * 该商务经理行业管理经验
     * business_manager_exp
     */
    private String businessManagerExp;

    /**
     * 在集团拖欠款项情况说明
     * bloc_debt_explain
     */
    private String blocDebtExplain;

    /**
     * 项目转包情况
     * item_sub_contract_condition
     */
    private String itemSubContractCondition;

    /**
     * 诉讼情况说明
     * lawsuit_condition_explain
     */
    private String lawsuitConditionExplain;

    /**
     * 该商务经理其他关注事项
     * other_notice_item
     */
    private String otherNoticeItem;

    /**
     * 初步方案-审核分析
     * pause_program_audit
     */
    private String pauseProgramAudit;

    /**
     * 初步方案-审核分析
     * cust_manager_audit
     */
    private String custManagerAudit;

    /**
     * 风控经理审核-审核分析
     * risk_manager_audit
     */
    private String riskManagerAudit;

    /**
     * 财务经理审核-审核分析
     * fund_manager_audit
     */
    private String fundManagerAudit;

    /**
     * 风控总经理审批分析
     * risk_manager_head_audit
     */
    private String riskManagerHeadAudit;
}