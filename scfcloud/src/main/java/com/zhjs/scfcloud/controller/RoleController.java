package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.RoleFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 用户组管理
 * @author liuchanghai
 */
@Api(consumes = "用户组Controller（角色）")
@RestController
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleFeignService roleFeignService;

    @ApiOperation("查询公司角色列表")
    @GetMapping("/company/role")
    @RequiresPermissions("common:role:tree")
    public Result getRoleListByCompanyId(){
        logger.info("查询公司角色列表:{}");
        //是否为超管
        boolean isSuperAdmin = false;
        Optional<Role> optional = MySubjectUtil.getUser().getRoleList().stream().filter(role -> role.getId().longValue() == 1l).findAny();
        if (optional != null && optional.isPresent()) {
            isSuperAdmin = true;
        }
        return roleFeignService.getRoleListByCompanyId(MySubjectUtil.getUserId(),isSuperAdmin);
    }

    @ApiOperation("查询用户组成员列表")
    @GetMapping("/role/{roleId}/user")
    @RequiresPermissions("common:role:user:list")
    public Result selectRoleUsers(@PathVariable Long roleId) {
        logger.info("查询用户组成员列表:roleId={}"+roleId);
        return roleFeignService.selectRoleUsers(roleId);
    }

    @ApiOperation("添加公司-用户组")
    @PostMapping("/company/{companyId}/role")
    @RequiresPermissions("common:role:add")
    public Result addRole(@PathVariable Long companyId, @RequestBody @Validated AddRoleDTO dto,BindingResult bindingResult){
        logger.info("添加公司-用户组:companyId={},{}",companyId,dto.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return roleFeignService.addRole(companyId,dto);
    }

    @ApiOperation("用户组是否存在")
    @PostMapping("/role/isExist")
    public Result isExist(@RequestBody IsExistDTO dto){
        return roleFeignService.isExist(dto);
    }

    @ApiOperation("编辑用户组")
    @PostMapping("/role/edit")
    @RequiresPermissions("common:role:edit")
    public Result aeditRole(@RequestBody EditRoleDTO dto){
        return roleFeignService.editRole(dto);
    }

    @ApiOperation("删除用户组")
    @DeleteMapping("/role/{roleId}")
    @RequiresPermissions("common:role:delete")
    public Result deleteRole(@PathVariable Long roleId){
        logger.info("删除公司-用户组:roleId={},{}",roleId);
        return roleFeignService.deleteRole(roleId);
    }

    @ApiOperation("添加用户组成员")
    @PostMapping("/role/{roleId}/user")
    @RequiresPermissions("common:role:user:add")
    public Result addRoleUser(@PathVariable Long roleId,@RequestBody @Validated AddRoleUserDTO dto,BindingResult bindingResult){
        logger.info("添加用户组成员:roleId={},{}",roleId,dto.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return roleFeignService.addRoleUser(roleId,dto);
    }

    @ApiOperation("删除用户组成员")
    @DeleteMapping("/role/user/{roleUserId}")
    public Result deleteRoleUser(@PathVariable Long roleUserId){
        logger.info("删除用户组成员:roleUserId={}",roleUserId);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return roleFeignService.deleteRoleUser(roleUserId,user.getId());
    }

    @ApiOperation("查询指定公司的角色")
    @PostMapping("/role/find/company/user")
    public Result findCompanyUserRole(@RequestBody UserIdAndCompanyIdDTO dto){
        // 获取userId
        dto.setUserId(MySubjectUtil.getUserId());
        logger.info(dto.toString());
        return roleFeignService.findCompanyUserRole(dto);
    }

    @ApiOperation("查询角色权限")
    @PostMapping("/role/find/privilege")
    @RequiresPermissions("common:role:permission:info")
    public Result findRolePrivilege(@RequestBody BaseIdDTO dto){
        logger.info(dto.toString());
        return roleFeignService.findRolePrivilege(dto);
    }

    @ApiOperation("编辑角色权限")
    @PostMapping("/role/edit/privilege")
    @RequiresPermissions("common:role:permission:edit")
    public Result editRolePrivilege(@RequestBody RoleFuncDTO dto){
        logger.info(dto.toString());
        //是否为超管
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        boolean isSuperAdmin = false;
        Optional<Role> optional = user.getRoleList().stream().filter(role -> role.getId().longValue() == 1l).findAny();
        if (optional != null && optional.isPresent()) {
            isSuperAdmin = true;
        }
        return roleFeignService.editRolePrivilege(dto,isSuperAdmin);
    }

    @ApiOperation("查询公司角色列表")
    @PostMapping("/role/findCompanyRoleList")
    public String findCompanyRoleList(@RequestBody RoleListDTO dto){
        logger.info("subject:{},dto:{}","查询公司角色列表", JsonUtil.toJSON(dto));
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        return roleFeignService.findCompanyRoleList(dto).toJSON();
    }

}
