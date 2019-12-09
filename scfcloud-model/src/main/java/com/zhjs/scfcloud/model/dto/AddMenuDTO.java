package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新建菜单 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:19
 *
 * @author liuchanghai
 * @create 2019-05-17 16:19
 * @since
 */

@Data
public class AddMenuDTO {

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单链接")
    private String menuUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("上级菜单")
    private Integer parentId;

    @ApiModelProperty("菜单级别")
    private Integer level;

    @ApiModelProperty("菜单状态: 0未激活1激活-1禁用.")
    private Integer status;
}
