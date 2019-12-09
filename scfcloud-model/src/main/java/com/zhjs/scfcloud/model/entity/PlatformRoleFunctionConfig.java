package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-22 11:58
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_platform_role_cunc")
public class PlatformRoleFunctionConfig {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 系统版本ID
     * system_version_id
     */
    private Long systemVersionId;

    /**
     * 角色名称
     * role_name
     */
    private String roleName;

    /**
     * 权限代码
     * funct_code
     */
    private String functCode;
}