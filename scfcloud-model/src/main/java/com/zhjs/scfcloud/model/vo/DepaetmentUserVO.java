package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepaetmentUserVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户邮箱")
    private String userEmail;

}
