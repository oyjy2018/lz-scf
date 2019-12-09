package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-28 16:33
 *
 * @author liuchanghai
 * @create 2019-05-28 16:33
 * @since
 */

@Data
public class CompanyRoleListVO {
    /**
     * 公司ID
     */
    private Long companyId;
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司拥有的角色集合
     */
    List<RoleVO> roles;
}
