package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 功能角色关联关系表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:02
 *
 * @author liuchanghai
 * @create 2019-05-17 14:02
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_role_function")
public class RoleFunction {
    /**
     * 主键ID.
     * id
     */
    private Long id;

    /**
     * 角色ID.
     * role_id
     */
    private Long roleId;

    /**
     * 角色ID.
     * system_id
     */
    private Long systemId;

    /**
     * 功能代码.
     * funct_code
     */
    private String functCode;
}
