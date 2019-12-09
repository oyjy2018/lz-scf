package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.entity.RoleFunction;

import java.util.List;

/**
 * 角色与功能关系管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 13:39
 *
 * @author liuchanghai
 * @create 2019-05-31 13:39
 * @since
 */
public interface RoleFunctionService extends IService<RoleFunction> {

    List<String> listRoleFunctionCodeByRoleId(Long roleId);
}
