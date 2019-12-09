package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CompanyAuditDetailDTO {
    //公司信息
    @ApiModelProperty("营业执照url")
    private String blicUrl;

    @ApiModelProperty("营业执照url-文件名")
    private String blicUrlFileName;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("信用代码")
    private String creditCode;

    @ApiModelProperty("省")
    private String provinceName;

    @ApiModelProperty("市")
    private String cityName;

    @ApiModelProperty("区")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detailAddr;

    @ApiModelProperty("营业期限")
    private String blicEndDate;

    @ApiModelProperty("所属行业")
    private String companyNature;

    @ApiModelProperty("具体行业")
    private String companyNatureConcrete;

    @ApiModelProperty("联系电话")
    private String contactNumber;

    @ApiModelProperty("公司规模")
    private String staffSize;

    //法人信息
    @ApiModelProperty("法人身份证-国徽页")
    private String legalPersonCertFrontUrl;

    @ApiModelProperty("法人身份证-国徽页-文件名")
    private String legalPersonCertFrontFileName;

    @ApiModelProperty("法人身份证-头像页")
    private String legalPersonCertBackUrl;

    @ApiModelProperty("法人身份证-头像页-文件名")
    private String legalPersonCertBackFileName;

    @ApiModelProperty("法人代表姓名")
    private String legalPersonName;

    @ApiModelProperty("法人代表证件号码")
    private String legalPersonCertNo;

    @ApiModelProperty("法人代表证件有效期止")
    private String legalPersonCertEndDate;

    @ApiModelProperty("法人代表与被授权人是否一致（0:否 1:是）")
    private Integer aupisLep;

    //被授权人信息
    @ApiModelProperty("被授权人身份证-国徽页")
    private String porxyPersonCertFrontUrl;

    @ApiModelProperty("被授权人身份证-国徽页-文件名")
    private String porxyPersonCertFrontFileName;

    @ApiModelProperty("被授权人身份证-头像页")
    private String porxyPersonCertBackUrl;

    @ApiModelProperty("被授权人身份证-头像页-文件名")
    private String porxyPersonCertBackFileName;

    @ApiModelProperty("被授权人姓名")
    private String porxyPersonName;

    @ApiModelProperty("被授权人证件号码")
    private String porxyPersonCertNo;

    @ApiModelProperty("被授权人代表证件有效期止")
    private String porxyPersonCertEndDate;

    @ApiModelProperty("授权委托书")
    private String porxyCommissionUrl;

    @ApiModelProperty("授权委托书-文件名")
    private String porxyCommissionFileName;

    //被授权人系统账号信息
    @ApiModelProperty("被授权人邮箱(默认系统账号)")
    private String porxyPersonEmail;

    @ApiModelProperty("被授权人手机号(系统账号)")
    private String porxyPersonPhone;

    @ApiModelProperty("被授权人账户登录密码")
    private String porxyPersonPassword;

    //审批信息
    @ApiModelProperty("注册系统ID列表;使用,号隔开(英文逗号)")
    private String systemIdList;

    @ApiModelProperty("业务流程;使用,号隔开(英文逗号)")
    private String flowList;

    @ApiModelProperty("公司类别")
    private String companyCategory;

    @ApiModelProperty("风控平台版本名称")
    private String riskControlSystemVersionName;

    @ApiModelProperty("商票交易平台版本名称")
    private String ticketSystemVersionName;
}
