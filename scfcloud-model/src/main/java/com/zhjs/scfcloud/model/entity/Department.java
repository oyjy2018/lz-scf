package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 部门表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:09
 *
 * @author liuchanghai
 * @create 2019-05-17 14:09
 * @since
 */


@Data
@Accessors(chain = true)
@TableName("scf_cfg_department")
public class Department {

    /**
     * 组织ID.
     */
    private Long id;

    /**
     * 组织名称.
     */
    private String deptName;

    /**
     * 组织等级.
     */
    private Integer deptLevel;

    /**
     * 组织负责人.
     */
    private String deptHead;

    /**
     * 上级组织ID.
     */
    private Long parentId;

    /**
     * 所属公司ID.
     */
    private Long companyId;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 更新时间.
     */
    private LocalDateTime updateTime;
}
