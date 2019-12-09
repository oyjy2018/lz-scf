package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑菜单 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:23
 *
 * @author liuchanghai
 * @create 2019-05-17 16:23
 * @since
 */

@Data
public class EditMenuDTO {

    @ApiModelProperty("菜单ID")
    private Integer id;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单链接")
    private String menuUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("上级菜单")
    private Integer parentId;
}
