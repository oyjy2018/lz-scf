package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.*;

/**
 * 受邀用户激活注册
 * 受邀用户激活注册dto
 *
 * @author oyjy
 */

@Data
public class UserAcceptInviteAndRegisterDTO {

    @ApiModelProperty(value = "邮箱",required = true)
    @NotBlank(message = "邮箱不能为空")
    //@Email(regexp = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$",message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "真实姓名不能为空")
    private  String userName;

    @ApiModelProperty(value = "密码",required = true)
    @Size(max = 16,min = 8,message = "密码为8-16位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "公司编码",required = true)
    @NotNull(message = "公司编码不能为空")
    private Long companyId;
}

