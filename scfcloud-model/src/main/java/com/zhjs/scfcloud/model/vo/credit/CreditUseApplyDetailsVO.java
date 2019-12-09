package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.model.entity.File;
import com.zhjs.scfcloud.model.vo.CommentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用信申请详情 视图展示数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 16:46
 *
 * @author liuchanghai
 * @create 2019-06-27 16:46
 * @since
 */

@Data
public class CreditUseApplyDetailsVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("流程CODE")
    private String flowCode;

    @ApiModelProperty("申请人名称")
    private String applyUserName;

    @ApiModelProperty("授信工程项目ID")
    private Long creditItemId;

    @ApiModelProperty("授信工程项目名称")
    private String creditItemName;

    @ApiModelProperty("申请业务种类")
    private String applyBusiness;

    @ApiModelProperty("申请用途")
    private String applyPurpose;

    @ApiModelProperty("申请用途-其他")
    private String applyPurposeOther;

    @ApiModelProperty("申请金额")
    private Integer applyMoney;

    @ApiModelProperty("申请期限")
    private Integer applyDeadline;

    @ApiModelProperty("还款计划")
    private String refundPlan;

    @ApiModelProperty("收票账户名称")
    private String ticketGetName;

    @ApiModelProperty("收票银行账号")
    private String ticketGetBankAccount;

    @ApiModelProperty("收票银行")
    private Integer ticketGetBank;

    @ApiModelProperty("收票银行省")
    private String ticketGetBankProvince;

    @ApiModelProperty("收票银行市")
    private String ticketGetBankCity;

    @ApiModelProperty("收票银行支行")
    private String ticketGetBankBranch;

    @ApiModelProperty("审批用信金额")
    private Integer auditMoney;

    @ApiModelProperty("审批用信期限")
    private Integer auditDeadline;

    @ApiModelProperty("附件列表")
    private List<File> fileList;

    @ApiModelProperty("评论列表")
    private List<CommentVO> commentList;
}
