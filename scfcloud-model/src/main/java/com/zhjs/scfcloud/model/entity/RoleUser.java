package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author:dailongting
 * @date:2019-07-22 11:51
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_role_user")
public class RoleUser {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 用户ID
     * user_id
     */
    private Long userId;

    /**
     * 角色ID
     * role_id
     */
    private Long roleId;

    /**
     * 修改人ID
     * update_by_id
     */
    private Long updateById;

    /**
     * 创建人ID
     * create_by_id
     */
    private Long createById;

    /**
     * 修改时间
     * update_time
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;
}