package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 只有一个参数的dto
 */

@Data
public class SingleParamDTO {

    //参数名
    private String param;

    //参数值
    @NotNull(message = "传值不能为空")
    private String value;
}
