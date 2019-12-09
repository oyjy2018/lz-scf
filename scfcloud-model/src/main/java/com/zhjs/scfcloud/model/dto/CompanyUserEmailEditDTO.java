package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserEmailEditDTO {

    @ApiModelProperty("邮箱")
    @NotEmpty(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty("验证码")
    @NotEmpty(message = "验证码不能为空")
    private String code;

}
