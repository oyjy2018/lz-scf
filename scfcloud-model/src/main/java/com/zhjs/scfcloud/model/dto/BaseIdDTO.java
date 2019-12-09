package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <功能描述>
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 18:16
 * @since
 */

@Data
public class BaseIdDTO {

    @ApiModelProperty("主键ID")
    private Long id;
}
