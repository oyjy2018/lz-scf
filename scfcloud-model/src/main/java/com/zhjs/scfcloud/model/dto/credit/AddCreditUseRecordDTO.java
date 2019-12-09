package com.zhjs.scfcloud.model.dto.credit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.model.annotation.IsLong;
import com.zhjs.scfcloud.model.annotation.IsNum;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:39
 *
 * @author liuchanghai
 * @create 2019-06-27 11:39
 * @since
 */
@Data
public class AddCreditUseRecordDTO implements Serializable {
    //入口 creditRecord:授信记录 ticketApply：开商票申请
    private String enter = "ticketApply";

    @ApiModelProperty("添加人ID")
    private Long userId;

    @ApiModelProperty("添加人")
    private String userName;

    @ApiModelProperty("用信申请ID")
    private Long useApplyId;

    //授信id
    private Long creditId;

    @ApiModelProperty("用信类型（0：开商票，1：工程贷）")
    private Integer useType;

    @ApiModelProperty("出票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ticketStart;

    @ApiModelProperty("到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date ticketEnd;

    @ApiModelProperty("票据号码")
    @NotNull(message = "票据号码不能为空")
    private String ticketNo;

    @ApiModelProperty("票据状态")
    private Integer ticketStatus;


    @ApiModelProperty("票据金额")
    @Pattern(regexp = "^\\d+(\\.\\d+)?$",message = "票据金额格式不正确")
    @DecimalMin(value = "0",inclusive = false,message = "票据金额必须大于0元")
    @DecimalMax(value = "1000000000",inclusive = false,message = "票据金额必须小于10亿元")
    private String ticketMoney;


    @ApiModelProperty("能否转让")
    @NotNull(message = "能否转让不能为空")
    private Integer isTransfer;

    @ApiModelProperty("交易合同号")
    private String tradeContractNo;

    @ApiModelProperty("出票人公司全称")
    @NotNull(message = "出票人公司全称不能为空")
    private String ticketGiveCompany;

    @ApiModelProperty("收票人公司全称")
    @NotNull(message = "收票人公司全称不能为空")
    private String ticketGetCompany;

    @ApiModelProperty("承兑人公司全称")
    @NotNull(message = "承兑人公司全称不能为空")
    private String acceptorCompany;

    @ApiModelProperty("承兑人账号")
    @NotNull(message = "承兑人账号不能为空")
    @Size(max = 19,message = "承兑人账号不能超过19位")
    @IsNum(message = "承兑人账号必须是数字")
    private String acceptorAccount;

    @ApiModelProperty("承兑人开户行")
    @NotNull(message = "承兑人开户行不能为空")
    private String acceptorBank;

    @ApiModelProperty("承兑人开户行行号")
    @NotNull(message = "承兑人开户行行号不能为空")
    private String acceptorBankNo;

    @ApiModelProperty("承兑人开户行省code")
    @NotNull(message = "承兑人开户行省code不能为空")
    private String acceptorBankProvinceCode;

    @ApiModelProperty("承兑人开户行市code")
    @NotNull(message = "承兑人开户行市code不能为空")
    private String acceptorBankCityCode;

    @ApiModelProperty("承兑人开户行支行")
    @NotNull(message = "承兑人开户行支行不能为空")
    private String acceptorBankBranch;

    //正面图片
    @NotBlank(message = "商票正面图片不能为空")
    private String ticketFrontImgUrl;

    //背面图片
    @NotBlank(message = "商票背面图片不能为空")
    private String ticketBackImgUrl;
}
