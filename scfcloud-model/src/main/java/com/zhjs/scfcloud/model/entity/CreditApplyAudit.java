package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-03 10:10
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_apply_audit")
public class CreditApplyAudit {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司id
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型id
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 业务id
     * business_id
     */
    private Long businessId;

    /**
     * 审核结果(1通过2退回3拒绝)
     * audit_result
     */
    private Integer auditResult;

    /**
     * 审批人ID
     * audit_person_id
     */
    private Long auditPersonId;

    /**
     * 审核人
     * audit_person
     */
    private String auditPerson;

    /**
     * 审批时间
     * audit_time
     */
    private Date auditTime;

    /**
     * 审批之前的流程
     * audit_before_flow
     */
    private String auditBeforeFlow;

    /**
     * 审批之后的流程
     * audit_after_flow
     */
    private String auditAfterFlow;

    /**
     * 总耗时
     * total_time_consuming
     */
    private String totalTimeConsuming;

    /**
     * 备注
     * remarks
     */
    private String remarks;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 审批意见
     * audit_opinion
     */
    private String auditOpinion;
}