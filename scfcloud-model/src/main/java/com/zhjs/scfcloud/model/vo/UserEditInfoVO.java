package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 编辑成员信息 视图数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-29 19:32
 *
 * @author liuchanghai
 * @create 2019-05-29 19:32
 * @since
 */

@Data
public class UserEditInfoVO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("机构类型（1：个人；2：机构）")
    private Integer permissionType;

    @ApiModelProperty("权限机构集合（逗号分割）")
    List<Long> permissionOrgIds;

    @ApiModelProperty("部门ID集合")
    private List<Long> deptIds;

    @ApiModelProperty("角色ID集合")
    private List<Long> roleIds;
}
