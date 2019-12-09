package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.RoleUser;
import com.zhjs.scfcloud.model.vo.RoleUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
     * 查询用户在公司拥有的角色ID
     * @param userId 用户ID
     * @param companyId 公司ID
     * @return
     */
    List<Long> findRoleIdList(Long userId, Long companyId);

    /**
     * 查询用户组成员
     * @param roleId 角色ID
     * @return
     */
    List<RoleUserVO> selectRoleUsers(Long roleId);

    /**
     * 用户删除角色
     * @param id 入参 用户ID
     * @param companyId 入参 公司ID
     * @return
     */
     boolean deleteByUserIdAndCompanyId(@Param("id") Long id, @Param("companyId") Long companyId);
}