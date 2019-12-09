

package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑数据字典 入参
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 18:14
 * @since
 */
@Data
public class EditDictDTO extends BaseIdDTO {

    @ApiModelProperty("值")
    private String value;
}
