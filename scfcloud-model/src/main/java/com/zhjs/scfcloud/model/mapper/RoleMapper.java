package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表 Mapper 接口.
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 11:25
 *
 * @author liuchanghai
 * @create 2019-05-17 11:25
 * @since
 */

public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询用户指定公司下的角色
     * @param userId 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
    List<Role> findUserRolesByCompanyId(@Param("userId")Long userId, @Param("companyId")Long companyId);

    /**
     * 查询用户在公司里拥有的角色
     * @param companyId 入参 公司ID
     * @param userId 入参 用户ID
     * @return
     */
    List<Role> findRoleListByCompanyIdAndUserId(Long companyId, Long userId);

    /**
     * 通过公司id和角色名查询角色
     * @param companyId
     * @param roleName
     * @return
     */
    Role getRoleByCompanyIdAndRoleName(Long companyId, String roleName);
}
