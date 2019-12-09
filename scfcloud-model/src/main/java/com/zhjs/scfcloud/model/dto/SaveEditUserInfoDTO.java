package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存编辑用户信息 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-30 13:19
 *
 * @author liuchanghai
 * @create 2019-05-30 13:19
 * @since
 */

@Data
public class SaveEditUserInfoDTO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("部门ids")
    private List<Long> deptIds;

    @ApiModelProperty("角色ids")
    private List<Long> roleIds;

    @ApiModelProperty("机构类型（1：个人；2：机构）")
    private Integer permissionType;

    @ApiModelProperty("权限机构集合（逗号分割）")
    List<String> permissionOrgIds;
}
