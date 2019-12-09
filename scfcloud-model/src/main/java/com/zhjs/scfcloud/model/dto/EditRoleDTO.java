package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑角色 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:50
 *
 * @author liuchanghai
 * @create 2019-05-17 16:50
 * @since
 */

@Data
public class EditRoleDTO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    private String remark;
}
