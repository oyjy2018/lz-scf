package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.CompanyRoleListVO;
import com.zhjs.scfcloud.model.vo.RolePrivilegeVO;
import com.zhjs.scfcloud.model.vo.RoleVO;
import com.zhjs.scfcloud.system.service.RoleService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户组管理
 * @author liuchanghai
 */

@Api(tags = "用户组Controller（角色）")
@RestController
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleService roleService;

    @ApiOperation("查询登录用户可查看公司角色集合")
    @GetMapping("/company/role")
    public Result findCompanyRoleList(@RequestParam("userId") Long userId,@RequestParam("isSuperAdmin") Boolean isSuperAdmin) {
        logger.info("用户查询用户组, userId:{},isSuperAdmin:{}", userId ,isSuperAdmin);
        List<CompanyRoleListVO> list = roleService.findCompanyRoleListByUserId(userId,isSuperAdmin);
        if(list.isEmpty()) return Result.failure("无有效公司");
        return Result.success(list);
    }

    @ApiOperation("查询用户组成员")
    @GetMapping("/role/{roleId}/user")
    public Result selectRoleUsers(@PathVariable Long roleId) {
        return Result.success(roleService.selectRoleUsers(roleId));
    }

    @ApiOperation("添加公司-用户组")
    @PostMapping("/company/{companyId}/role")
    public Result add(@PathVariable Long companyId,@RequestBody AddRoleDTO dto) {
        logger.info("添加公司-用户组:companyId={},{} ",companyId,dto.toString());
        return roleService.addCompanyRole(companyId,dto);
    }

    @ApiOperation("是否存在角色")
    @PostMapping("/role/isExist")
    public Result isExist(@Valid @RequestBody IsExistDTO dto) {
        logger.info("是否存在角色 " + dto.toString());
        boolean isExist = roleService.isExist(dto);
        if(isExist){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("编辑角色")
    @PostMapping("/role/edit")
    public Result edit(@RequestBody EditRoleDTO dto) {
        logger.info("编辑角色 " + dto.toString());
        return roleService.edit(dto);
    }

    @ApiOperation("删除用户组")
    @DeleteMapping("/role/{roleId}")
    public Result deleteRole(@PathVariable Long roleId) {
        logger.info("删除公司-用户组:roleId={},{}",roleId);
        Role role = roleService.getById(roleId);
        if(role.getIsDelete() == 0){
            return Result.failure("当前用户组不可删除");
        }
        boolean result = roleService.removeById(roleId);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("添加用户组成员")
    @PostMapping("/role/{roleId}/user")
    public Result addRoleUser(@PathVariable Long roleId,@RequestBody AddRoleUserDTO dto){
        logger.info("添加用户组成员:roleId={},{}",roleId);
        roleService.addRoleUser(roleId,dto);
        return Result.success();
    }

    @ApiOperation("删除用户组成员")
    @DeleteMapping("/role/{roleUserId}/user/{userId}")
    public Result deleteRoleUser(@PathVariable Long roleUserId,@PathVariable Long userId){
        logger.info("删除用户组成员:roleUserId={},userId={}",roleUserId,userId);
        return roleService.deleteRoleUserById(roleUserId,userId);
    }

    @ApiOperation("查询指定公司的角色")
    @PostMapping("/role/find/company/user")
    public Result findCompanyUserRole(@RequestBody UserIdAndCompanyIdDTO dto) {
        logger.info("用户查询用户组 " + dto.toString());
//        List<RoleVO> list = roleService.findRoleListByCompanyIdAndUserId(dto.getCompanyId(), dto.getUserId());
        List<RoleVO> list = roleService.findList(dto.getCompanyId());
        return Result.success(list);
    }

    /**
     * 查询公司角色列表（功能同上  PS：上面的方法命名不规范，以后用此方法）
     * @param dto
     * @return
     */
    @ApiOperation("查询公司角色列表")
    @PostMapping("/role/findCompanyRoleList")
    public Result findCompanyRoleList(@RequestBody RoleListDTO dto){
        logger.info("subject:{},dto:{}","查询公司角色列表", JsonUtil.toJSON(dto));
        List<RoleVO> list = roleService.findList(dto.getCompanyId());
        return Result.success(list);
    }

    @ApiOperation("查询角色权限")
    @PostMapping("/role/find/privilege")
    public Result findRolePrivilege(@RequestBody BaseIdDTO dto){
        logger.info("查询角色权限 " + dto.toString());
        RolePrivilegeVO rolePrivilegeVO =  roleService.findRolePrivilege(dto.getId());
        return Result.success(rolePrivilegeVO);
    }

    @ApiOperation("编辑角色权限")
    @PostMapping("/role/edit/privilege")
    public Result editRolePrivilege(@RequestBody RoleFuncDTO dto,@RequestParam("isSuperAdmin") Boolean isSuperAdmin){
        logger.info("编辑角色权限,isSuperAdmin:{},dto:{}",isSuperAdmin,dto.toString());

        return roleService.editRolePrivilege(dto,isSuperAdmin);
    }

    //通过公司id和角色名查询角色
    @PostMapping("/role/getRoleByCompanyIdAndRoleName")
    public Result getRoleByCompanyIdAndRoleName(Long companyId, String roleName) {
        logger.info("subject:{},companyId:{},roleName:{}", "通过公司id和角色名查询角色id", companyId, roleName);
        return roleService.getRoleByCompanyIdAndRoleName(companyId, roleName);
    }

    /**
     * 查询用户在指定公司下的角色
     * @param companyId
     * @param userId
     * @return
     */
    @PostMapping("/role/findRoleListByCompanyIdAndUserId")
    public Result<List<Role>> findRoleListByCompanyIdAndUserId(@RequestParam Long companyId, @RequestParam Long userId){
        logger.info("subject:{},companyId:{},userId:{}", "查询用户在指定公司下的角色", companyId, userId);
        List<Role> roleList = roleService.findRoleListByCompanyIdAndUserId(companyId,userId);
        return Result.success(roleList);
    }

    /**
     * 获取角色的文件资料附件权限
     * @param userId
     * @return
     */
    @GetMapping("/role/filePermissions")
    public Result findRoleFilePermissions(@RequestParam Long userId){
        return Result.success(roleService.getRoleFilePermissionsByUserId(userId));
    }
}
