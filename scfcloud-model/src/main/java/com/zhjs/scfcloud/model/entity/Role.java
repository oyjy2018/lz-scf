package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 11:19
 *
 * @author liuchanghai
 * @create 2019-05-17 11:19
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_role")
public class Role implements Serializable {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 公司ID.
     */
    private Long companyId;

    /**
     * 角色名称.
     */
    private String roleName;

    /**
     * 角色描述.
     */
    private String remark;

    /**
     * 角色状态;角色状态: 0未激活1激活3禁用.
     */
    private Integer status;

    /**
     * 是否有可修改权限0不可修改1可以修改
     */
    private Integer isEditPrivilege;

    /**
     * 是否可删除0不删除1可以删除
     */
    private Integer isDelete;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 更新时间.
     */
    private LocalDateTime updateTime;
}
