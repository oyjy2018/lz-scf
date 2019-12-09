package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserInfoEditDTO {


    @ApiModelProperty("真实姓名")
    @NotEmpty(message = "真实姓名不能为空")
    @Size(max = 50,message = "真实姓名过长")
    private String userName;

    @ApiModelProperty("真实姓名")
    private Integer gender;

}
