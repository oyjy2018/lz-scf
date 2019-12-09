package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-06 11:43
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_item")
public class CreditItem {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 授信申请ID
     * credit_apply_id
     */
    private Long creditApplyId;

    /**
     * 授信工程项目
     * item_name
     */
    private String itemName;

    /**
     * 施工进度
     * construction_schedule
     */
    private BigDecimal constructionSchedule;

    /**
     * 预计完工时间
     * plan_finish_time
     */
    private Date planFinishTime;

    /**
     * 项目收款账期
     * item_collection_days
     */
    private Integer itemCollectionDays;

    /**
     * 申请授信额度
     * apply_credit_money
     */
    private Integer applyCreditMoney;

    /**
     * 授信审批金额
     * audit_credit_money
     */
    private Integer auditCreditMoney;

    /**
     * 申请业务种类（授信成功后）
     * apply_credit_business
     */
    private String applyCreditBusiness;

    /**
     * 申请用途
     * apply_purpose
     */
    private String applyPurpose;

    /**
     * 申请用途-其他
     * apply_purpose_other
     */
    private String applyPurposeOther;

    /**
     * 是否分次支用
     * has_divide_use
     */
    private Integer hasDivideUse;

    /**
     * 授信开始时间
     * credit_start_date
     */
    private Date creditStartDate;

    /**
     * 授信结束时间
     * credit_end_date
     */
    private Date creditEndDate;

    /**
     * 本次支用金额
     * the_use_money
     */
    private Integer theUseMoney;

    /**
     * 用信要求
     * use_credit_require
     */
    private String useCreditRequire;

    /**
     * 授信条件
     * credit_conditions
     */
    private String creditConditions;

    /**
     * 用信计划
     * use_credit_plan
     */
    private String useCreditPlan;

    /**
     * 是否发生过质量安全事故
     * has_occur_safety_accident
     */
    private Integer hasOccurSafetyAccident;

    /**
     * 质量安全事故说明
     * safety_accident_explain
     */
    private String safetyAccidentExplain;

    /**
     * 《项目承包责任书》是否已签字确认
     * has_sign_duty_book
     */
    private Integer hasSignDutyBook;

    /**
     * 描述项目经营风险
     * risk_explain
     */
    private String riskExplain;

    /**
     * 描述项目管控措施
     * controls_explain
     */
    private String controlsExplain;

    /**
     * 项目其他关注事项
     * other_notice_item
     */
    private String otherNoticeItem;

    /**
     * 相同授信申请的第几个项目
     * item_index
     */
    private Integer itemIndex;

    /**
     * 
     * update_time
     */
    private Date updateTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * create_by
     */
    private Long createBy;
}