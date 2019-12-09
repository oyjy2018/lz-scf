package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.CompanyRoleListVO;
import com.zhjs.scfcloud.model.vo.RolePrivilegeVO;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.model.vo.RoleUserVO;

import java.util.List;

/**
 * 角色管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:46
 *
 * @author liuchanghai
 * @create 2019-05-17 16:46
 * @since
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取用户有效的公司角色集合
     * @param userId
     * @param isSuperAdmin
     * @return
     */
    List<CompanyRoleListVO> findCompanyRoleListByUserId(Long userId, Boolean isSuperAdmin);

    /**
     * 查询用户组成员
     * @param roleId
     * @return
     */
    List<RoleUserVO> selectRoleUsers(Long roleId);

    /**
     * 添加公司-用户组
     * @param companyId
     * @param dto
     * @return
     */
    Result addCompanyRole(Long companyId,AddRoleDTO dto);

    /**
     * 用户组添加成员
     * @param roleId
     * @param dto
     */
    void addRoleUser(Long roleId,AddRoleUserDTO dto);

    /**
     * 删除用户组成员
     * @param roleUserId 角色用户ID
     * @param userId 用户ID
     * @return
     */
    Result deleteRoleUserById(Long roleUserId,Long userId);

    Result edit(EditRoleDTO dto);

    boolean isExist(IsExistDTO dto);

    /**
     * 获取角色列表
     * @param companyId 公司ID
     * @return
     */
    List<RoleVO> findList(Long companyId);

    /**
     * 根据公司ID及用户ID获取用户在公司里拥有的角色
     * @param companyId 入参 公司ID
     * @param userId 入参 用户ID
     * @return
     */
    List<Role> findRoleListByCompanyIdAndUserId(Long companyId, Long userId);

    /**
     * 根据角色ID查询角色的权限
     * @param roleId 入参 角色ID
     * @return
     */
    RolePrivilegeVO findRolePrivilege(Long roleId);

    /**
     * 保存角色的权限
     * @param dto 入参
     * @param isSuperAdmin
     * @return
     */
    Result editRolePrivilege(RoleFuncDTO dto, Boolean isSuperAdmin);


    /**
     * 通过公司id和角色名查询角色
     * @param companyId
     * @param roleName
     * @return
     */
    Result getRoleByCompanyIdAndRoleName(Long companyId, String roleName);

    /**
     * 获取角色的文件资料附件权限
     * @param userId
     * @return
     */
    List<String> getRoleFilePermissionsByUserId(Long userId);
}
