package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 是否存在菜单入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:23
 *
 * @author liuchanghai
 * @create 2019-05-17 16:23
 * @since
 */

@Data
public class IsExistMenuDTO {

    @ApiModelProperty("菜单名称")
    private String menuName;
}
