package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserPasswordDTO {

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

}
