package com.zhjs.scfcloud.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  公司审核列表展示数据实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 18:07
 *
 * @author liuchanghai
 * @create 2019-05-21 18:07
 * @since
 */

@Data
public class CompanyAuditListVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("信用代码")
    private String creditCode;

    @ApiModelProperty("法人代表姓名")
    private String legalPersonName;


    @ApiModelProperty("申请人")
    private String porxyPersonName;

    @ApiModelProperty("手机号码")
    private String porxyPersonPhone;

    @ApiModelProperty("注册系统ID列表;使用,号隔开(英文逗号)")
    private String systemIdList;

    @ApiModelProperty("风控平台版本名称")
    private String riskSystemVersionName;

    @ApiModelProperty("票据交易平台版本名称")
    private String ticketSystemVersionName;

    @ApiModelProperty("审核状态:0待审核1审核通过2审核不通过")
    private Integer status;

    @ApiModelProperty("申请加入时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
