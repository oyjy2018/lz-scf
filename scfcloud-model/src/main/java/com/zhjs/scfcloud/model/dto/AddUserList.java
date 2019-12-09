package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 邀请用户 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-29 17:22
 *
 * @author liuchanghai
 * @create 2019-05-29 17:22
 * @since
 */

@Data
public class AddUserList {

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("机构类型（1：个人；2：机构）")
    private Integer permissionType;

    @ApiModelProperty("用户邮箱")
    List<String> emails;

    @ApiModelProperty("角色ID")
    List<Long> roleIds;

    @ApiModelProperty("部门ID")
    List<Long> deptIds;

    @ApiModelProperty("权限机构集合（逗号分割）")
    List<String> permissionOrgIds;
}
