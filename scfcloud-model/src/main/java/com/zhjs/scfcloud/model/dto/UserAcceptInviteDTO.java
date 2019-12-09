package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 受邀用户激活注册
 * 受邀用户激活注册dto
 *
 * @author oyjy
 */

@Data
public class UserAcceptInviteDTO {

    @ApiModelProperty(value = "邮箱",required = true)
    @NotBlank(message = "邮箱不能为空")
    //@Email(regexp = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$",message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "公司编码",required = true)
    @NotNull(message = "公司编码不能为空")
    private Long companyId;
}

