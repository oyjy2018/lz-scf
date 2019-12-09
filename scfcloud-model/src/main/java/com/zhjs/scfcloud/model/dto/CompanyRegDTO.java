package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户注册的 参数
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 15:08
 * @since
 */

@Data
public class CompanyRegDTO {
    //注册系统
    @ApiModelProperty("注册系统ID列表;使用,号隔开(英文逗号)")
    @NotEmpty(message = "注册系统ID列表不能为空")
    private String  systemIdList;

    //公司信息
    @ApiModelProperty("营业执照url")
    @NotEmpty(message = "营业执照url不能为空")
    private String blicUrl;

    @ApiModelProperty("公司名称")
    @NotEmpty(message = "公司名称不能为空")
    private String companyName;

    @ApiModelProperty("信用代码")
    @NotEmpty(message = "信用代码不能为空")
    @Size(max = 20,message = "统一社会信用代码过长")
    private String creditCode;

    @ApiModelProperty("省")
    @NotEmpty(message = "省份不能为空")
    private String provinceName;

    @ApiModelProperty("市")
    @NotEmpty(message = "市不能为空")
    private String cityName;

    @ApiModelProperty("区")
    private String districtName;

    @ApiModelProperty("详细地址")
    @NotEmpty(message = "详细地址不能为空")
    private String detailAddr;

    @ApiModelProperty("营业期限")
    @NotEmpty(message = "营业期限不能为空")
    private String blicEndDate;

    @ApiModelProperty("所属行业")
    @NotEmpty(message = "所属行业不能为空")
    private String companyNature;

    @ApiModelProperty("具体行业")
    @NotEmpty(message = "具体行业不能为空")
    private String companyNatureConcrete;

    @ApiModelProperty("联系电话")
    @NotEmpty(message = "联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty("公司规模")
    @NotEmpty(message = "公司规模不能为空")
    private String staffSize;

    //法人信息
    @ApiModelProperty("法人身份证-国徽页")
    @NotEmpty(message = "法人身份证-国徽页，文件地址不能为空")
    private String legalPersonCertFrontUrl;

    @ApiModelProperty("法人身份证-头像页")
    @NotEmpty(message = "法人身份证-头像页，文件地址不能为空")
    private String legalPersonCertBackUrl;

    @ApiModelProperty("法人代表姓名")
    @NotEmpty(message = "法人代表姓名不能为空")
    private String legalPersonName;

    @ApiModelProperty("法人代表证件号码")
    @NotEmpty(message = "法人代表证件号码不能为空")
    private String legalPersonCertNo;

    @ApiModelProperty("法人代表证件有效期止")
    @NotEmpty(message = "法人代表证件有效期不能为空")
    private String legalPersonCertEndDate;

    @ApiModelProperty("法人代表与被授权人是否一致（0:否 1:是）")
    @NotNull(message = "法人代表与被授权人是否一致，不能为空")
    private Integer aupisLep;

    //被授权人信息
    @ApiModelProperty("被授权人身份证-国徽页")
    @NotEmpty(message = "被授权人身份证-国徽页，不能为空")
    private String porxyPersonCertFrontUrl;

    @ApiModelProperty("被授权人身份证-头像页")
    @NotEmpty(message = "被授权人身份证-头像页，不能为空")
    private String porxyPersonCertBackUrl;

    @ApiModelProperty("被授权人姓名")
    @NotEmpty(message = "被授权人姓名不能为空")
    private String porxyPersonName;

    @ApiModelProperty("被授权人证件号码")
    @NotEmpty(message = "被授权人证件号码不能为空")
    private String porxyPersonCertNo;

    @ApiModelProperty("被授权人代表证件有效期止")
    @NotNull(message = "被授权人代表证件有效期不能为空")
    private String porxyPersonCertEndDate;

    @ApiModelProperty("授权委托书")
    private String porxyCommissionUrl;

    //被授权人系统账号信息
    @ApiModelProperty("被授权人邮箱(默认系统账号)")
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String porxyPersonEmail;

    @ApiModelProperty("被授权人手机号(系统账号)")
    @NotEmpty(message = "手机号不能为空")
    private String porxyPersonPhone;

    @ApiModelProperty("被授权人账户登录密码")
    @NotEmpty(message = "登录密码不能为空")
    private String porxyPersonPassword;

}
