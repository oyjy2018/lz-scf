package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.SystemVersionFunction;
import com.zhjs.scfcloud.model.vo.MenuFunctionTreeVO;
import com.zhjs.scfcloud.model.vo.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统版本与系统功能关联关系信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 08:55
 *
 * @author liuchanghai
 * @create 2019-05-22 08:55
 * @since
 */
public interface SystemVersionFunctionMapper extends BaseMapper<SystemVersionFunction> {

    /**
     * 查询系统菜单
     * @param versionIds
     * @return
     */
    List<MenuVO> selectMenuList(@Param("parentId") Long parentId,@Param("versionIds") List<Long> versionIds);

    /**
     * 根据菜单ID查询菜单功能
     * @param menuId
     * @return
     */
    List<MenuFunctionTreeVO> selectFunctionList(@Param("menuId") Long menuId,@Param("versionIds") List<Long> versionIds);
}
