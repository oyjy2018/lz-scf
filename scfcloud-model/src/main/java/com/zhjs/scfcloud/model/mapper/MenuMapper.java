package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:17
 *
 * @author liuchanghai
 * @create 2019-05-17 14:17
 * @since
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取角色拥有的菜单
     * @param roles
     * @return
     */
    List<String> findHaveMenu(@Param("roles")String roles,@Param("parentIds")String parentIds);
}
