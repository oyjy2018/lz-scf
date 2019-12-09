package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuVO {
    /**
     * 菜单ID
     * id
     */
    private Long menuId;

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
     * 级别
     * level
     */
    private Integer level;

    /**
     * 下级菜单列表
     */
    List<MenuVO> menuList;

    /**
     * 菜单拥有的功能集合
     */
    List<MenuFunctionTreeVO> functionList;
}
