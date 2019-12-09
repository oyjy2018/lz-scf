package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.SystemFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统功能信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:18
 *
 * @author liuchanghai
 * @create 2019-05-17 14:18
 * @since
 */
public interface SystemFunctionMapper extends BaseMapper<SystemFunction> {

    List<SystemFunction> findFuncListByRoleIds(@Param("roleIds")String roleIds);

    /**
     * 获取文件附件权限集合
     * @return
     */
    List<String> selectCodesByFileMenu(@Param("roleIds")String roleIds);
}
