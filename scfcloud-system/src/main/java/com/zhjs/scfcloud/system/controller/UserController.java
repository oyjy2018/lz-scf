package com.zhjs.scfcloud.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserEditInfoVO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.model.vo.UserListVO;
import com.zhjs.scfcloud.system.service.CompanyService;
import com.zhjs.scfcloud.system.service.UserService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册;用户登录 控制器
 */

@Api(tags = "用户管理(用户登录/注册)")
@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @ApiOperation("根据用户ID查询用户")
    @PostMapping("/getUserById")
    public Result<User> getUserById(@RequestParam Long userId){
        logger.info("根据用户ID查询用户:dto:{}", JsonUtil.toJSON(userId));
        try{
            User user = userService.selectOne(new Object[]{"id",userId});
            if(user == null) return Result.failure("用户不存在");
            return Result.success(user);
        }catch (Exception e){
            logger.error("根据用户ID查询用户异常：{}",e.getMessage());
            return Result.failure("根据用户ID查询用户异常");
        }
    }

    @ApiOperation("根据手机查询用户")
    @PostMapping("/getUserByPhone")
    public Result<User> getUserByPhone(@RequestParam String phone){
        logger.info("根据手机查询用户:dto:{}", JsonUtil.toJSON(phone));
        try{
            User user = userService.selectOne(new Object[]{"phone",phone,"is_del",0});
            if(user == null) return Result.failure("用户不存在");
            return Result.success(user);
        }catch (Exception e){
            logger.error("根据手机查询用户异常：{}",e.getMessage());
            return Result.failure("根据手机查询用户异常");
        }
    }

    @ApiOperation("根据邮箱查询用户")
    @PostMapping("/getUserByEmail")
    public Result<User> getUserByEmail(@RequestParam String email){
        logger.info("根据邮箱查询用户:dto:{}", JsonUtil.toJSON(email));
        try{
            User user = userService.selectOne(new Object[]{"email",email,"is_del",0});
            if(user == null) return Result.failure("用户不存在");
            return Result.success(user);
        }catch (Exception e){
            logger.error("根据邮箱查询用户异常：{}",e.getMessage());
            return Result.failure("根据邮箱查询用户异常");
        }
    }

    @ApiOperation("获取用户信息(登录)")
    @PostMapping("/getUserInfo")
    public Result<UserInfoVO> getUserInfo(@RequestBody UserLoginDTO dto){
        logger.info("获取用户详细信息:dto:{}", JsonUtil.toJSON(dto));
        try{
            UserInfoVO userInfoVO = userService.getUserInfo(dto.getUserId(), dto.getCompanyId());
            if(userInfoVO == null) return Result.failure("用户不存在");
            return Result.success(userInfoVO);
        }catch (Exception e){
            logger.error("获取用户详细信息异常：{}",e.getMessage());
            return Result.failure("获取用户详细信息异常");
        }
    }

    @ApiOperation("查询公司成员(主要用于下拉选择)")
    @PostMapping("findByCompanyId")
    public Result findByCompanyId(@RequestBody CompanyIdDTO dto){
        logger.info(dto.toString());
        return userService.findByCompanyId(dto.getCompanyId());
    }

    @ApiOperation("查询成员管理列表")
    @PostMapping("findUserlist")
    public Result findUserlist(@RequestBody FindUserListDTO dto){
        logger.info(dto.toString());
        Page<UserListVO> result = userService.findUserlist(dto);
        return Result.success(result);
    }

    @ApiOperation("邀请添加多个成员")
    @PostMapping("add/list")
    public Result addUserList(@RequestBody AddUserList addUserList){
        logger.info("addUserList:{}",addUserList.toString());
        return userService.addUserList(addUserList);
    }

    @ApiOperation("查询编辑用户的信息")
    @PostMapping("edit/find/info")
    public Result findEditUserInfo(@RequestBody UserIdAndCompanyIdDTO dto){
        logger.info(dto.toString());
        UserEditInfoVO info = userService.findEditUserInfo(dto.getUserId(), dto.getCompanyId());
        return Result.success(info);
    }

    @ApiOperation("编辑用户的信息")
    @PostMapping("edit/info/save")
    public Result saveEditUserInfo(@RequestBody SaveEditUserInfoDTO dto){
        logger.info(dto.toString());
        boolean result = userService.saveEditUserInfo(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("编辑公司用户状态")
    @PostMapping("edit/status")
    public Result editUserStausByUserId(@RequestBody CompanyUserStatusDTO dto){
        logger.info(dto.toString());
        boolean result = userService.updateCompanyUserByCompanyUserId(dto.getCompanyUserId(), dto.getStatus());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除公司用户")
    @PostMapping("delete")
    public Result deleteById(@RequestBody CompanyUserIdDTO dto){
        logger.info("subject:{},dto:{}","删除公司用户",dto.toString());
        return userService.deleteCompanyUserByCompanyUserId(dto.getCompanyUserId());
    }

    @ApiOperation("用户接受邀请并注册（新用户）")
    @PostMapping("acceptInviteAndRegister")
    public Result acceptInviteAndRegister(@RequestBody UserAcceptInviteAndRegisterDTO dto) {
        return userService.acceptInviteAndRegister(dto);
    }

    //用户接受邀请（老用户）
    @PostMapping("acceptInvite")
    public Result acceptInvite(@RequestBody UserAcceptInviteDTO dto) {
        return userService.acceptInvite(dto);
    }

    //修改密码
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestParam("phone") String phone,
                                 @RequestParam("newPassword") String newPassword) {
        return userService.updatePassword(phone,newPassword);
    }

    //检测手机号是否注册
    @PostMapping("isRegisterPhone")
    public Result isRegisterPhone(@RequestParam("phone") String phone) {
        boolean isRegister = userService.phoneCheck(phone);
        Map retMap = new HashMap();
        retMap.put("isRegister",isRegister);
        return Result.success(retMap);
    }

    @ApiOperation("检测邮箱是否注册")
    @GetMapping("/{email}/isRegister")
    public Result isRegisterEmail(@PathVariable String email) {
        logger.info("检测手机号是否注册,email:{}",email);
        return Result.success(userService.emailCheck(email));
    }

    //检测是为新用户
    @GetMapping("/checkIsNewUser")
    public Result checkIsNewUser(@RequestParam("email") String email) {
        logger.info("检测是为新用户,email:{}",email);
        return userService.checkIsNewUser(email);
    }

    //再次发送邀请邮件
    @PostMapping("reSendInvite")
    public Result reSendInvite(@RequestBody UserReInviteDTO userReInviteDTO) {
        logger.info("subject:{},userReInviteDTO:{}","再次发送邀请邮件",JsonUtil.toJSON(userReInviteDTO));
        return userService.reSendInvite(userReInviteDTO);
    }

    @ApiOperation("我的详情")
    @PostMapping("/myDetailsByUserId")
    public Result myDetailsByUserId(@RequestBody BaseIdDTO dto) {
        return userService.myDetailsByUserId(dto.getId());
    }

    @ApiOperation("修改邮箱")
    @PostMapping("/updateEmailByUserId")
    public Result updateEmailByUserId(@RequestBody Map<String, Object> dto) {
        logger.info("修改邮箱{} " + dto.toString());
        Long userId = Long.parseLong(dto.get("userId").toString());
        String email = (String) dto.get("email");
        return userService.updateEmailByUserId(userId, email);
    }

    @ApiOperation("修改手机号")
    @PostMapping("/updatePhoneByUserId")
    public Result updatePhoneByUserId(@RequestBody Map<String, Object> dto) {
        logger.info("修改手机号{} " + dto.toString());
        Long userId = Long.parseLong(dto.get("userId").toString());
        String phone = (String) dto.get("phone");
        return userService.updatePhoneByUserId(userId, phone);
    }

    @ApiOperation("密码输入是否正确")
    @PostMapping("/isPassword")
    public Result isPassword(@RequestBody Map<String, Object> dto) {
        logger.info("密码输入是否正确{} " + dto.toString());
        Long userId = Long.parseLong(dto.get("userId").toString());
        String password = (String) dto.get("password");
        boolean result = userService.isPassword(userId, password);
        if(result){
            return Result.success("密码正确");
        }
        return Result.failure("密码错误");
    }

    @ApiOperation("查询用户的数据权限")
    @PostMapping("findUserDataPermissionList")
    public Result findUserDataPermissionList(@RequestBody UserIdAndCompanyIdDTO dto) {
        return userService.findUserDataPermissionList(dto.getUserId());
    }

    @ApiOperation("修改用户")
    @PostMapping("updateUser")
    public Result updateUser(@RequestBody User user){
        logger.info("密码输入是否正确{} ",JsonUtil.toJSON(user));
        if(!userService.updateById(user)){
            return Result.failure("修改用户失败");
        }
        return Result.success();
    }

}
