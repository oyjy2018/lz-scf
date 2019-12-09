package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统功能信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 11:55
 *
 * @author liuchanghai
 * @create 2019-05-17 11:55
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_system_function")
public class SystemFunction implements Serializable {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 功能代码.
     */
    private String funcCode;

    /**
     * 功能名称.
     */
    private String funcName;

    /**
     * 功能描述.
     */
    private String remark;

    /**
     * 菜单ID.
     */
    private Long menuId;

    /**
     * 功能状态;功能状态: 0未启用1已启用.
     */
    private Integer status;

    /**
     * 系统版本ID
     */
     private Long systemVersionId;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 更新时间.
     */
    private LocalDateTime updateTime;
}
