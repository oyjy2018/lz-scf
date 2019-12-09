package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新建数据字典 入参
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 18:03
 * @since
 */

@Data
public class AddDictDTO {

    @ApiModelProperty("键")
    private String key;

    @ApiModelProperty("值")
    private String value;
}
