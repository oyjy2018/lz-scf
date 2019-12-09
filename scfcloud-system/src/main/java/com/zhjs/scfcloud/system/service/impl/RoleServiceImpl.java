package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.RoleMapper;
import com.zhjs.scfcloud.model.mapper.RoleUserMapper;
import com.zhjs.scfcloud.model.mapper.SystemFunctionMapper;
import com.zhjs.scfcloud.model.transfer.RoleTransfer;
import com.zhjs.scfcloud.model.vo.CompanyRoleListVO;
import com.zhjs.scfcloud.model.vo.RolePrivilegeVO;
import com.zhjs.scfcloud.model.vo.RoleUserVO;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.system.service.*;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyStatusEnum;
import com.zhjs.scfcloud.util.enums.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理的业务逻辑接口实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:47
 *
 * @author liuchanghai
 * @create 2019-05-17 16:47
 * @since
 */

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleTransfer roleTransfer;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleFunctionService roleFunctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private SystemFunctionMapper systemFunctionMapper;

    @Override
    public List<CompanyRoleListVO> findCompanyRoleListByUserId(Long userId, Boolean isSuperAdmin) {
        User user = userService.getById(userId);
        //查询用户的公司数据集合
        List<Long> companyIds = new ArrayList<>();
        // 根据机构类型 判定用户的公司访问集合
        if(user.getPermissionType() == UserEnum.PermissionType.PermissionType1.getStatus()){
            //个人
            QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
            companyUserQueryWrapper.eq("user_id",userId);
            companyIds.add(companyUserService.getOne(companyUserQueryWrapper).getCompanyId());
        }else{
            //机构
            if(StringUtils.isEmpty(user.getPermissionOrgIds())) return null;
            for(String element : user.getPermissionOrgIds().split(",")){
                companyIds.add(new Long(element));
            }
        }

        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.ne("status", CompanyStatusEnum.status2.getValue());
        companyQueryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        companyQueryWrapper.in("id",companyIds);
        List<Company> companyList = companyService.list(companyQueryWrapper);
        List<CompanyRoleListVO> result = new ArrayList<>(companyList.size());
        // 获取公司的角色
        for (Company source: companyList){
            CompanyRoleListVO target = new CompanyRoleListVO();
            target.setCompanyId(source.getId());
            target.setCompanyName(source.getCompanyName());
            //设置公司的角色集合
            QueryWrapper<Role> where = new QueryWrapper<Role>();
            where.eq("company_id", source.getId());
            List<Role> list = list(where);
            List<RoleVO> roles = roleTransfer.roles2RoleVO(list);
            //超管 可编辑其他管理员(即可编辑所有角色)
            if (isSuperAdmin) {
                roles.stream().forEach(roleVO -> {
                    //if (roleVO.getId().longValue() == 1l) return; //超管自身不能编辑
                    roleVO.setIsEditPrivilege(CommonEnum.WhetherEnum.YES.getStatus());
                });
            }
            target.setRoles(roles);

            result.add(target);
        }
        return result;
    }

    @Override
    public List<RoleUserVO> selectRoleUsers(Long roleId) {
        return roleUserMapper.selectRoleUsers(roleId);
    }

    @Override
    public Result addCompanyRole(Long companyId, AddRoleDTO dto) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("company_id",companyId);
        roleQueryWrapper.eq("role_name", dto.getRoleName());
        if(count(roleQueryWrapper) > 0) return Result.failure("用户组已存在");
        Role role = new Role();
        role.setCompanyId(companyId)
                .setRoleName(dto.getRoleName())
                .setRemark(dto.getRemark())
                .setIsEditPrivilege(CommonEnum.WhetherEnum.YES.getStatus())
                .setIsDelete(CommonEnum.WhetherEnum.YES.getStatus())
                .setStatus(1)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        save(role);
        return Result.success();
    }

    @Override
    public void addRoleUser(Long roleId, AddRoleUserDTO dto) {
        List<RoleUser> roleUsers = new ArrayList<>();
        for (Long userId: dto.getUserIds()){
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(roleId)
                    .setUserId(userId)
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now());
            roleUsers.add(roleUser);
        }
        roleUserService.saveBatch(roleUsers);
    }

    @Override
    public Result deleteRoleUserById(Long roleUserId,Long userId) {
        RoleUser roleUser = roleUserService.getById(roleUserId);
        if(roleUser.getUserId() == userId){
            return Result.failure("不可删除本人");
        }
        //查看当前用户组
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>();
        roleQueryWrapper.eq("id", roleUser.getRoleId());
        Role role = getOne(roleQueryWrapper);
        if("公司管理员".equals(role.getRoleName())){
            QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<RoleUser>();
            roleUserQueryWrapper.eq("role_id",roleUser.getRoleId());
            if(roleUserService.count(roleUserQueryWrapper) == 1){
                return Result.failure("当前用户组为公司管理员，且当前用户组只有一人，不可删除");
            }
        }
        roleUserService.removeById(roleUserId);
        return Result.success();
    }

    /**
     * 编辑角色
     * @param dto 入参
     * @return
     */
    @Override
    public Result edit(EditRoleDTO dto) {

        Role role = getById(dto.getId());
        if(role.getIsEditPrivilege() == 0){
            return Result.failure("当前用户组权限不可修改");
        }
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("company_id",role.getCompanyId());
        roleQueryWrapper.eq("role_name", dto.getRoleName());
        if(count(roleQueryWrapper) > 0) return Result.failure("用户组已存在");

        role.setRoleName(dto.getRoleName()).setRemark(dto.getRemark());
        updateById(role);
        return Result.success();
    }

    /**
     * 角色是否存在
     * @param dto
     * @return
     */
    @Override
    public boolean isExist(IsExistDTO dto) {
        LambdaQueryWrapper<Role> lambda = new QueryWrapper<Role>().lambda();
        lambda.eq(Role::getCompanyId, dto.getId());
        lambda.eq(Role::getRoleName, dto.getName());
        List<Role> list = list(lambda);
        return list.size() > 0 ? true : false;
    }

    /**
     * 查询角色列表
     * @param companyId 入参
     * @return
     */
    @Override
    public List<RoleVO> findList(Long companyId) {
        LambdaQueryWrapper<Role> lambda = new QueryWrapper<Role>().lambda();
        lambda.eq(Role::getCompanyId, companyId);
        List<Role> list = list(lambda);
        List<RoleVO> result = roleTransfer.roles2RoleVO(list);
        return result;
    }

    /**
     * 根据公司ID及用户ID获取用户在公司里拥有的角色
     * @param companyId 入参 公司ID
     * @param userId 入参 用户ID
     * @return
     */
    @Override
    public List<Role> findRoleListByCompanyIdAndUserId(Long companyId, Long userId) {
        return roleMapper.findRoleListByCompanyIdAndUserId(companyId, userId);
    }

    /**
     * 根据角色ID查询角色的权限
     * @param roleId 入参 角色ID
     * @return
     */
    @Override
    public RolePrivilegeVO findRolePrivilege(Long roleId) {
        RolePrivilegeVO rolePrivilegeVO = new RolePrivilegeVO();
        List<String> functCodes = roleFunctionService.listRoleFunctionCodeByRoleId(roleId);
        rolePrivilegeVO.setFunctCodes(functCodes);
        return rolePrivilegeVO;
    }

    /**
     * 编辑角色的权限
     * @param dto 入参
     * @param isSuperAdmin
     * @return
     */
    @Override
    public Result editRolePrivilege(RoleFuncDTO dto, Boolean isSuperAdmin) {
        Role role = getById(dto.getRoleId());
        // 判断角色的权限是否可修改
        //非超管不能修改管理员
        if(role.getIsEditPrivilege() == 0 && !isSuperAdmin){
            return Result.failure("管理员权限不可修改");
        }
        // 删除旧的权限数据
        QueryWrapper<RoleFunction> deleteWhere = new QueryWrapper<RoleFunction>();
        deleteWhere.eq("role_id", dto.getRoleId());
        if(dto.getSystemId() == 2){
            deleteWhere.in("system_id",2,4);
        }else{
            deleteWhere.notIn("system_id",2,4);
        }
        boolean remove = roleFunctionService.remove(deleteWhere);
        // 保存新的权限数据
        if(dto.getFunctions().isEmpty()){
            return Result.success();
        }
        List<RoleFunction> addList = new ArrayList<>();
        for (FunctionDTO code: dto.getFunctions()) {
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRoleId(dto.getRoleId())
                    .setFunctCode(code.getFunctCode())
                    .setSystemId(code.getSystemId());
            addList.add(roleFunction);
        }
        return roleFunctionService.saveBatch(addList) ? Result.success() : Result.failure("保存新权限失败，请联系管理员");
    }

    /**
     * 通过公司id和角色名查询角色
     * @param companyId
     * @param roleName
     * @return
     */
    @Override
    public Result getRoleByCompanyIdAndRoleName(Long companyId, String roleName) {
        Role role = roleMapper.getRoleByCompanyIdAndRoleName(companyId,roleName);
        if (role == null) {
            return Result.failure("未查询到角色信息");
        }
        return Result.success(role);
    }

    @Override
    public List<String> getRoleFilePermissionsByUserId(Long userId) {
        //获取用户所有角色
        QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<>();
        roleUserQueryWrapper.eq("user_id", userId);
        List<RoleUser> roleUsers = roleUserService.list(roleUserQueryWrapper);
        String roleIds = roleUsers.stream().map(roleUser -> roleUser.getRoleId()+"").collect(Collectors.joining(","));
        //获取文件附件权限集合
        return systemFunctionMapper.selectCodesByFileMenu(roleIds);
    }
}
