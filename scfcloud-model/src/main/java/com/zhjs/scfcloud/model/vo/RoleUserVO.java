package com.zhjs.scfcloud.model.vo;

import lombok.Data;

@Data
public class RoleUserVO {

    /**
     * 角色用户ID
     */
    private Long roleUserId;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户邮箱
     */
    private String userEmail;
}
