package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色展示数据实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 16:33
 *
 * @author liuchanghai
 * @create 2019-05-20 16:33
 * @since
 */

@Data
public class RoleVO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色类型0平台创建的1用户创建的")
    private Long roleType;

    @ApiModelProperty("角色描述")
    private String roleRemark;

    @ApiModelProperty("是否可编辑权限")
    private Integer isEditPrivilege;

    @ApiModelProperty("是否可删除")
    private Integer isDelete;
}
