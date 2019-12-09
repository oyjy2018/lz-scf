package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserPhoneEditDTO {

    @ApiModelProperty("手机号")
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;

}
