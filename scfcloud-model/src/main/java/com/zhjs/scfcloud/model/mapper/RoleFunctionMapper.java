package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.RoleFunction;

import java.util.List;

/**
 * 角色系统功能关联关系信息表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:16
 *
 * @author liuchanghai
 * @create 2019-05-17 14:16
 * @since
 */
public interface RoleFunctionMapper extends BaseMapper<RoleFunction> {

    /**
     * 查询角色拥有的功能代码
     * @param roleId 角色ID
     * @return
     */
    List<String> listFunctCodesBy(Long roleId);
}
