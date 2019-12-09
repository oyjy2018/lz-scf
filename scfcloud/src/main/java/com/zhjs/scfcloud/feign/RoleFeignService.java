package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 19:10
 *
 * @author liuchanghai
 * @create 2019-05-27 19:10
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "role")
public interface RoleFeignService {

    @ApiOperation("查询登录用户可查看公司角色集合")
    @GetMapping("/company/role")
    Result getRoleListByCompanyId(@RequestParam("userId") Long userId, @RequestParam("isSuperAdmin") Boolean isSuperAdmin);

    @ApiOperation("查询用户组成员")
    @GetMapping("/role/{roleId}/user")
    Result selectRoleUsers(@PathVariable Long roleId);

    @ApiOperation("添加公司用户组")
    @PostMapping("/company/{companyId}/role")
    Result addRole(@PathVariable Long companyId,@RequestBody AddRoleDTO dto);

    @ApiOperation("角色是否存在")
    @PostMapping("/role/isExist")
    Result isExist(IsExistDTO dto);

    @ApiOperation("编辑角色")
    @PostMapping("/role/edit")
    Result editRole(@RequestBody EditRoleDTO dto);

    @ApiOperation("删除用户组")
    @DeleteMapping("/role/{roleId}")
    Result deleteRole(@PathVariable Long roleId);

    @ApiOperation("添加用户组成员")
    @PostMapping("/role/{roleId}/user")
    Result addRoleUser(@PathVariable Long roleId,@RequestBody AddRoleUserDTO dto);

    @ApiOperation("删除用户组成员")
    @DeleteMapping("/role/{roleUserId}/user/{userId}")
    Result deleteRoleUser(@PathVariable Long roleUserId,@PathVariable Long userId);

    @ApiOperation("查询指定公司的角色")
    @PostMapping("/role/find/company/user")
    Result findCompanyUserRole(@RequestBody UserIdAndCompanyIdDTO dto);

    /**
     * 查询公司角色列表（功能同上  PS：上面的方法命名不规范，以后用此方法）
     * @param dto
     * @return
     */
    @ApiOperation("查询公司角色列表")
    @PostMapping("/role/findCompanyRoleList")
    Result findCompanyRoleList(@RequestBody RoleListDTO dto);

    @ApiOperation("查询角色权限")
    @PostMapping("/role/find/privilege")
    Result findRolePrivilege(@RequestBody BaseIdDTO dto);

    @ApiOperation("编辑角色权限")
    @PostMapping("/role/edit/privilege")
    Result editRolePrivilege(@RequestBody RoleFuncDTO dto, @RequestParam("isSuperAdmin") Boolean isSuperAdmin);

    //通过公司id和角色名查询 角色
    @PostMapping("/role/getRoleByCompanyIdAndRoleName")
    Result getRoleByCompanyIdAndRoleName(@RequestParam("companyId") Long companyId, @RequestParam("roleName") String roleName);

    //通过公司id和角色名查询 角色
    @GetMapping("/role/filePermissions")
    Result findRoleFilePermissions(@RequestParam Long userId);
}
