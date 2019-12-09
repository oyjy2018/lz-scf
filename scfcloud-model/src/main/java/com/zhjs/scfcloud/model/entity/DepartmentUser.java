package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 部门与用户关联关系表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 18:35
 *
 * @author liuchanghai
 * @create 2019-05-21 18:35
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_department_user")
public class DepartmentUser {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 部门ID.
     */
    private Long deptId;

    /**
     * 用户ID.
     */
    private Long userId;


    /**
     * 状态.
     */
    private Integer status;

}
