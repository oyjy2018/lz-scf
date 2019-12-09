package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.RoleFunction;
import com.zhjs.scfcloud.model.mapper.RoleFunctionMapper;
import com.zhjs.scfcloud.system.service.RoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色与功能关系管理的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 13:39
 *
 * @author liuchanghai
 * @create 2019-05-31 13:39
 * @since
 */

@Service
public class RoleFunctionServiceImpl extends ServiceImpl<RoleFunctionMapper, RoleFunction> implements RoleFunctionService {

    @Autowired
    private RoleFunctionMapper roleFunctionMapper;

    @Override
    public List<String> listRoleFunctionCodeByRoleId(Long roleId) {
        return roleFunctionMapper.listFunctCodesBy(roleId);
    }
}
