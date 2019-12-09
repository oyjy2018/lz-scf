package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据字典是否存在键入参
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 17:57
 * @since
 */

@Data
public class IsExistDictDTO {

    @ApiModelProperty("键")
    private String dictKey;
}
