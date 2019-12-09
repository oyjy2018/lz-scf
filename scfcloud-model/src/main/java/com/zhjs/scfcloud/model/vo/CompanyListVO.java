package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公司管理列表 视图参数
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 10:34
 *
 * @author liuchanghai
 * @create 2019-05-22 10:34
 * @since
 */

@Data
public class CompanyListVO {

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("信用代码")
    private String creditCode;

    @ApiModelProperty("法人")
    private String legalPersonName;

    @ApiModelProperty("风控平台版本名称")
    private String riskSystemVersionName;

    @ApiModelProperty("票据交易平台版本名称")
    private String ticketSystemVersionName;

    @ApiModelProperty("公司成员数")
    private Integer userCount;

    @ApiModelProperty("公司状态")
    private Integer status;

    @ApiModelProperty("是否已进行京东企业注册(0:否 1:是)")
    private Integer isJdRegister;

    @ApiModelProperty("是否已进行京东企业认证(0:否 1:是)")
    private Integer jdVerified;

    @ApiModelProperty("是否已设置收票账户(0:否 1:是)")
    private Integer isReceiptAccount;

    @ApiModelProperty("是否已设置收款账户(0:否 1:是)")
    private Integer isRepayAccount;

    @ApiModelProperty("是否已进行e签宝企业注册(0:否 1:是)")
    private Integer esignVerified;

}
