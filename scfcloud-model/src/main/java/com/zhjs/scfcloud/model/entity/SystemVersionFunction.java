package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统版本与功能关联关系表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 18:29
 *
 * @author liuchanghai
 * @create 2019-05-21 18:29
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_system_version_function")
public class SystemVersionFunction {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 系统版本ID.
     */
    private Long versionId;

    /**
     * 功能ID.
     */
    private Long functionId;

    /**
     * 状态.
     */
    private Integer status;
}
