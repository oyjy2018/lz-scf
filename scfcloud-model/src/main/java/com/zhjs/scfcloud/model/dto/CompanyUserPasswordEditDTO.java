package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserPasswordEditDTO {

    @ApiModelProperty("旧密码")
    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

}
