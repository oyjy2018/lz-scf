package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * 审核日记 视图数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-19 13:56
 *
 * @author liuchanghai
 * @create 2019-06-19 13:56
 * @since
 */

@Data
public class CreditAuditVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("授信申请ID")
    private Long creditApplyId;

    @ApiModelProperty("审核人")
    private String auditPerson;

    @ApiModelProperty("审批意见")
    private String auditOpinion;

    @ApiModelProperty("审核时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    @ApiModelProperty("审批之前的流程")
    private String auditBeforeFlow;

    @ApiModelProperty("审批之后的流程")
    private String auditAfterFlow;

    @ApiModelProperty("总耗时")
    private String totalTimeConsuming;

    @ApiModelProperty("备注")
    private String remarks;
}
