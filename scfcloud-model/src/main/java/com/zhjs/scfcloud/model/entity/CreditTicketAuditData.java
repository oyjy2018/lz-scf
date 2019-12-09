package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-03 19:57
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_ticket_audit_data")
public class CreditTicketAuditData {
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
     * business_id
     */
    private Long businessId;

    /**
     * 风控总经理-审批金额
     * risk_manager_head_audit_money
     */
    private BigDecimal riskManagerHeadAuditMoney;

    /**
     * 风控总经理-审批期限
     * risk_manager_head_audit_deadline
     */
    private String riskManagerHeadAuditDeadline;

    /**
     * 
     * status
     */
    private Integer status;

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

    /**
     * 风控总经理-审批分析
     * risk_manager_head_audit_analyze
     */
    private String riskManagerHeadAuditAnalyze;
}