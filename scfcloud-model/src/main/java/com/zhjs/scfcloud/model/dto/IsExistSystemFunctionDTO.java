package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 是否不存在系统功能入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 11:25
 *
 * @author liuchanghai
 * @create 2019-05-20 11:25
 * @since
 */

@Data
public class IsExistSystemFunctionDTO {

    @ApiModelProperty("功能名称")
    private String funcName;
}
