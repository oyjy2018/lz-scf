package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑系统功能 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 11:24
 *
 * @author liuchanghai
 * @create 2019-05-20 11:24
 * @since
 */
@Data
public class EditSystemFunctionDTO extends BaseIdDTO {

    @ApiModelProperty("功能名称")
    private String funcName;

    @ApiModelProperty("功能描述")
    private String remark;

    @ApiModelProperty("菜单ID")
    private Integer menuId;
}
