package com.zhjs.scfcloud.model.vo.credit;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/8/28 11:29
 */
@Data
public class CreditApprovalCourseVO {
    /**
     * 审核结果(1通过2退回3拒绝)
     * audit_result
     */
//    private Integer auditResult;

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
    private String auditTime;

    /**
     * 审批之前的流程
     * audit_before_flow
     */
    private String auditBeforeFlow;

    private String auditBeforeFlowCH;

    /**
     * 审批之后的流程
     * audit_after_flow
     */
    private String auditAfterFlow;

    private String auditAfterFlowCH;

    /**
     * 总耗时
     * total_time_consuming
     */
    private String totalTimeConsuming;

    /**
     * 审批字段集合
     */
    List<Map<String, Object>> applyColumnList;
}
