package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用信申请审批完成vo
 */
@Data
public class CreditUseApplyAuditFinishListVO {



    @ApiModelProperty("申请ID")
    private Long id;

    //公司id
    private Long companyId;

    //业务类型
    private Long businessTypeId;

    //流程code
    private String flowCode;

    //用信类型
    private String useType;


    @ApiModelProperty("申请人")
    private String applyUserName;

    @ApiModelProperty("用信项目")
    private String creditItemName;

    @ApiModelProperty("业务类型")
    private String applyBusiness;

    @ApiModelProperty("申请用信金额")
    private String applyMoney;

    @ApiModelProperty("申请用信期限")
    private String applyDeadline;

    @ApiModelProperty("审批用信金额")
    private String auditMoney;

    @ApiModelProperty("审批用信期限")
    private String auditDeadline;

    @ApiModelProperty("实际用信金额")
    private String useMoney;

    @ApiModelProperty("申请时间")
    @JSONField(format="yyyy-MM-dd")
    private Date applyTime;

    @ApiModelProperty("关联授信金额")
    private Integer creditMoney;

    @ApiModelProperty("关联授信ID")
    private Long creditApplyId;

    @ApiModelProperty("关联授信ID")
    private Long creditId;

}
