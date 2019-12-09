package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author:dailongting
 * @date:2019-07-29 11:29
 */
@Data
@Accessors(chain = true)
@TableName("scf_menu")
public class Menu implements Serializable {
    /**
     * 菜单ID
     * id
     */
    private Long id;

    /**
     * 上级ID
     * parent_id
     */
    private Long parentId;

    /**
     * 系统ID
     * system_id
     */
    private Long systemId;

    /**
     * 菜单名称
     * menu_name
     */
    private String menuName;

    /**
     * 菜单url
     * menu_url
     */
    private String menuUrl;

    /**
     * 备注
     * remark
     */
    private String remark;

    /**
     * 级别
     * level
     */
    private Integer level;

    /**
     * 同级排序
     * sort
     */
    private Integer sort;

    /**
     * 状态(0未激活 1激活 2禁用)
     * status
     */
    private Integer status;

    /**
     * 修改人ID
     * update_by_id
     */
    private Integer updateById;

    /**
     * 创建人ID
     * create_by_id
     */
    private Integer createById;

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