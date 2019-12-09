package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用信申请列表 视图展示数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 14:05
 *
 * @author liuchanghai
 * @create 2019-06-27 14:05
 * @since
 */
@Data
public class CreditUseApplyListWebVO {

    private Long companyId;

    private Long businessTypeId;

    @ApiModelProperty("申请ID")
    private Long id;

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
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("流程状态")
    private String flowCode;

    @ApiModelProperty("流程状态")
    private String flowName;

    @ApiModelProperty("关联授信金额")
    private Integer creditMoney;

    @ApiModelProperty("关联授信ID")
    private Long creditApplyId;

    @ApiModelProperty("关联授信ID")
    private Long creditId;

    //是否已经使用
    private Integer hasUse;

    private String disposeRoleNames;

    private String disposeUserNames;

    private String showDisposeRoleNames;

    private String showDisposeUserNames;

    public String getShowDisposeRoleNames() {
        return StringUtil.getBreviaryName(disposeRoleNames,",",2);
    }

    public String getShowDisposeUserNames() {
        return StringUtil.getBreviaryName(disposeUserNames,",",2);
    }

}
