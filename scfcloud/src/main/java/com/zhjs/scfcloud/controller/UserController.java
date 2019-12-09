package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.RoleFeignService;
import com.zhjs.scfcloud.feign.UserFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.Role;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.enums.UserEnum;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: dailongting
 * @date:2019/5/23 13:46
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private RoleFeignService roleFeignService;

    /**
     * 权限验证(弃用)
     * @param dto
     * @return
     */
    @PostMapping("/isPermitted")
    @Deprecated
    public String isPermitted(@RequestBody UserDTO dto){
        logger.info("权限验证:{}", JsonUtil.toJSON(dto));
        try{
            if(dto.getFuncCodeList() == null || dto.getFuncCodeList().isEmpty()) return Result.failure("无效的权限校验数据").toJSON();

            List<Permission> permissionList = dto.funcCodeList.stream().map(funcCode -> {
                return new WildcardPermission(funcCode);
            }).collect(Collectors.toList());

            Subject subject = SecurityUtils.getSubject();
            List<String> retPermission = new ArrayList<>();
            boolean[] result = subject.isPermitted(permissionList);
            for(int i = 0;i < result.length; i++){
                if(result[i]) retPermission.add(dto.funcCodeList.get(i));
            }
            return Result.success(retPermission).toJSON();
        }catch (Exception e){
            logger.error("权限校验异常:{}", e.getMessage());
            return Result.failure("权限校验异常").toJSON();
        }
    }

    @ApiOperation("查询公司成员(主要用于下拉选择)")
    @PostMapping("findByCompanyId")
    public Result findByCompanyId(@RequestBody CompanyIdDTO dto){
        logger.info(dto.toString());
        return userFeignService.findByCompanyId(dto);
    }

    @ApiOperation("查询用户所在公司成员")
    @PostMapping("findCompanyUser")
    public String findCompanyUser(@RequestBody CompanyIdDTO dto){
        logger.info("subject:{},dto:{}","查询用户所在公司成员", JsonUtil.toJSON(dto));
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        return userFeignService.findByCompanyId(dto).toJSON();
    }

    @ApiOperation("成员管理")
    @PostMapping("find/list")
    @RequiresPermissions("common:user:list")
    public Result findUserList(@RequestBody FindUserListDTO dto) {
        logger.info("查询成员管理 " + dto.toString());
        return userFeignService.findUserlistByCompanyIdOrdeptId(dto);
    }

    @ApiOperation("添加用户(邀请多个)")
    @PostMapping("add/list")
    @RequiresPermissions("common:user:invite")
    public Result addUserList(@RequestBody AddUserList dto) {
        //登录用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到用户信息");
        dto.setUserName(user.getUserName());
        return userFeignService.addUserList(dto);
    }

    @ApiOperation("查询编辑用户的信息")
    @PostMapping("edit/find/info")
    @RequiresPermissions("common:user:info")
    public Result findEditUserInfo(@RequestBody UserIdAndCompanyIdDTO dto) {
        return userFeignService.findEditUserInfo(dto);
    }

    @ApiOperation("保存编辑用户的信息")
    @PostMapping("edit/info/save")
    @RequiresPermissions("common:user:edit")
    public Result saveEditUserInfo(@RequestBody SaveEditUserInfoDTO dto) {
        return userFeignService.saveEditUserInfo(dto);
    }

    @ApiOperation("编辑用户的状态")
    @PostMapping("edit/status")
    @RequiresPermissions("common:user:editStatus")
    public Result editUserStausByUserId(@RequestBody CompanyUserStatusDTO dto) {
        return userFeignService.editUserStausByUserId(dto);
    }

    @ApiOperation("删除用户")
    @PostMapping("delete")
    @RequiresPermissions("common:user:delete")
    public Result deleteByUserId(@RequestBody CompanyUserIdDTO dto) {
        return userFeignService.deleteByUserId(dto);
    }

    @ApiOperation("用户接受邀请并注册（新用户）")
    @PostMapping("acceptInviteAndRegister")
    public Result acceptInviteAndRegister(@RequestBody @Valid UserAcceptInviteAndRegisterDTO userAcceptInviteAndRegisterDTO, BindingResult bindingResult) {
        logger.info("subject:{},userAcceptInviteAndRegisterDTO:{}","用户接受邀请并加入（新用户）", userAcceptInviteAndRegisterDTO.toString());
        //参数校验
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            //返回错误信息
            return Result.failure(message);
        }
        if (!StringUtil.matchPwdReg(userAcceptInviteAndRegisterDTO.getPassword()))
            return Result.failure("密码必须包含大小写字母、数字、符号中至少3种");
        return userFeignService.acceptInviteAndRegister(userAcceptInviteAndRegisterDTO);
    }

    //用户接受邀请（老用户）
    @PostMapping("acceptInvite")
    public Result acceptInvite(@RequestBody @Valid UserAcceptInviteDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},dto:{}","用户接受邀请并加入（老用户）", dto.toString());
        //参数校验
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            //返回错误信息
            return Result.failure(message);
        }
        return userFeignService.acceptInvite(dto);
    }

    @ApiOperation("修改密码")
    @PostMapping("updatePassword")
    public Result updatePassword(@RequestBody Map<String,String> dataMap) {
        logger.info("subject:{},dataMap:{}","修改密码",dataMap);
        if (StringUtils.isEmpty(dataMap.get("phone")))
            return Result.failure("手机号不能为空");
        String phone = dataMap.get("phone");
        if (!StringUtil.isPhone(phone))
            return Result.failure("手机号格式不正确");
        if (StringUtils.isEmpty(dataMap.get("newPassword")))
            return Result.failure("新密码不能为空");
        String newPassword = dataMap.get("newPassword");
        if (newPassword.length() <8 || newPassword.length() > 16)
            return Result.failure("密码为8-16位");
        if (!StringUtil.matchPwdReg(newPassword))
            return Result.failure("密码必须包含大小写字母、数字、符号中至少3种");
        return userFeignService.updatePassword(phone,newPassword);
    }

    @ApiOperation("邀请多个用户(只传邮箱)")
    @PostMapping("inviteUsers")
    @RequiresPermissions("common:user:invite")
    public Result inviteUsers(@RequestBody Map<String,String> dataMap) {
        logger.info("subject:{},dataMap:{}","邀请多个用户(只传邮箱)",JsonUtil.toJSON(dataMap));
        if (dataMap == null || StringUtils.isEmpty(dataMap.get("emails"))){
            return Result.failure("参数缺失");
        }
        String emails = dataMap.get("emails");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到用户信息");
        if (StringUtils.isEmpty(emails))
            return Result.failure("邮箱不能为空");
        //查询当前公司的用户组：公司普通员工的用户组id
        Result result = roleFeignService.getRoleByCompanyIdAndRoleName(user.getCompanyId(),"公司普通员工");
        if (result.getCode() == Result.RESULT_CODE_FAILURE) {
            return result;
        }
        AddUserList dto = new AddUserList();
        Long roleId = JsonUtil.jsonToBean(JsonUtil.toJSON(result.getData()),Role.class).getId();
        dto.setUserName(user.getUserName());
        dto.setCompanyId(user.getCompanyId());
        dto.setEmails(Arrays.asList(emails.split(",")));
        dto.setRoleIds(Arrays.asList(roleId));
        dto.setDeptIds(new ArrayList<>());
        dto.setPermissionType(UserEnum.PermissionType.PermissionType1.getStatus());

        return userFeignService.addUserList(dto);
    }

    @ApiOperation("检测手机号是否注册")
    @PostMapping("isRegisterPhone")
    public Result isRegisterPhone(@RequestBody Map<String,String> dataMap) {
        logger.info("subject:{},phone:{}","检测手机号是否注册",JsonUtil.toJSON(dataMap));
        if (dataMap == null || org.springframework.util.StringUtils.isEmpty(dataMap.get("phone"))) {
            return Result.failure("参数缺失");
        }
        String phone = dataMap.get("phone");
        logger.info("subject:{},phone","检测手机号是否注册",phone);
        return userFeignService.isRegisterPhone(phone);
    }

    @ApiOperation("检测邮箱是否注册")
    @GetMapping("/{email}/isRegister")
    public Result isRegisterEmail(@PathVariable String email) {
        logger.info("检测手机号是否注册,email:{}",email);
        return userFeignService.isRegisterEmail(email);
    }

    @ApiOperation("检测是为新用户")
    @GetMapping("/checkIsNewUser")
    public Result checkIsNewUser(@RequestParam("email") String email) {
        logger.info("检测是为新用户,email:{}",email);
        return userFeignService.checkIsNewUser(email);
    }

    @ApiOperation("再次发送邀请邮件")
    @PostMapping("reSendInvite")
    public Result reSendInvite(@RequestBody @Valid UserReInviteDTO userReInviteDTO,BindingResult bindingResult) {
        logger.info("subject:{},userReInviteDTO:{}","再次发送邀请邮件",JsonUtil.toJSON(userReInviteDTO));
        //参数校验
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            //返回错误信息
            return Result.failure(message);
        }
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息");
        userReInviteDTO.setCompanyId(userInfoVO.getCompanyId());
        userReInviteDTO.setUserName(userInfoVO.getUserName());

        return userFeignService.reSendInvite(userReInviteDTO);
    }

    @ApiOperation("查询用户的数据权限")
    @PostMapping("findUserDataPermissionList")
    public Result findUserDataPermissionList() {
        logger.info("subject:{},findUserDataPermissionList:{}","查询用户的数据权限");
        UserIdAndCompanyIdDTO dto = new UserIdAndCompanyIdDTO();
        dto.setUserId(MySubjectUtil.getUser().getId());
        return userFeignService.findUserDataPermissionList(dto);
    }

}
