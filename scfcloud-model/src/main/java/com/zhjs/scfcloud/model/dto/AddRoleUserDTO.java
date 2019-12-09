package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddRoleUserDTO {

    @ApiModelProperty("用户ID")
    @NotEmpty(message = "请选择用户")
    private List<Long> userIds;
}
