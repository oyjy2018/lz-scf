package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddRoleDTO {

    @ApiModelProperty("用户组名称")
    @NotEmpty(message = "用户组名称不能为空")
    private  String roleName;

    @ApiModelProperty("用户组简介")
    private String remark;
}
