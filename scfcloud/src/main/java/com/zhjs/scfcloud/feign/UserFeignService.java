package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/5/21 16:24
 */
@FeignClient(value = "scfcloud-system", contextId = "user")
public interface UserFeignService {

    @ApiOperation("根据用户ID查询用户")
    @PostMapping("/user/getUserById")
    Result<User> getUserById(@RequestParam Long userId);

    @ApiOperation("根据手机查询用户")
    @PostMapping("/user/getUserByPhone")
    Result<User> getUserByPhone(@RequestParam String phone);

    @ApiOperation("根据邮箱查询用户")
    @PostMapping("/user/getUserByEmail")
    Result<User> getUserByEmail(@RequestParam String email);

    @ApiOperation("获取用户详细信息")
    @PostMapping("/user/getUserInfo")
    Result<UserInfoVO> getUserInfo(@RequestBody UserLoginDTO dto);

    @ApiOperation("根据公司ID或部门ID查询成员管理列表")
    @PostMapping("/user/findUserlist")
    Result findUserlistByCompanyIdOrdeptId(@RequestBody FindUserListDTO dto);

    @ApiOperation("添加用户(邀请多个)")
    @PostMapping("/user/add/list")
    Result addUserList(@RequestBody AddUserList dto);

    @ApiOperation("查询编辑用户的信息")
    @PostMapping("/user/edit/find/info")
    Result findEditUserInfo(@RequestBody UserIdAndCompanyIdDTO dto);

    @ApiOperation("保存编辑用户的信息")
    @PostMapping("/user/edit/info/save")
    Result saveEditUserInfo(@RequestBody SaveEditUserInfoDTO dto);

    @ApiOperation("编辑用户的状态")
    @PostMapping("/user/edit/status")
    Result editUserStausByUserId(@RequestBody CompanyUserStatusDTO dto);

    @ApiOperation("删除用户")
    @PostMapping("/user/delete")
    Result deleteByUserId(@RequestBody CompanyUserIdDTO dto);

    //用户接受邀请并注册（新用户）
    @PostMapping("/user/acceptInviteAndRegister")
    Result acceptInviteAndRegister(@RequestBody UserAcceptInviteAndRegisterDTO dto);

    //用户接受邀请（老用户）
    @PostMapping("/user/acceptInvite")
    Result acceptInvite(@RequestBody UserAcceptInviteDTO dto);

    //修改密码
    @PostMapping("/user/updatePassword")
    Result updatePassword(@RequestParam("phone") String phone, @RequestParam("newPassword") String newPassword);

    //检测手机号是否注册
    @PostMapping("/user/isRegisterPhone")
    Result isRegisterPhone(@RequestParam("phone") String phone);

    //检测手机号是否注册
    @GetMapping("/user/{email}/isRegister")
    Result isRegisterEmail(@PathVariable String email);

    //检测是为新用户
    @GetMapping("/user/checkIsNewUser")
    Result checkIsNewUser(@RequestParam("email") String email);

    //再次发送邀请邮件
    @PostMapping("/user/reSendInvite")
    Result reSendInvite(@RequestBody UserReInviteDTO userReInviteDTO);

    @ApiOperation("查询公司成员(主要用于下拉选择)")
    @PostMapping("/user/findByCompanyId")
    Result findByCompanyId(@RequestBody CompanyIdDTO dto);

    @ApiOperation("我的详情")
    @PostMapping("/user/myDetailsByUserId")
    Result myDetailsByUserId(@RequestBody BaseIdDTO dto);

    @ApiOperation("修改邮箱")
    @PostMapping("/user/updateEmailByUserId")
    Result updateEmailByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("修改手机号")
    @PostMapping("/user/updatePhoneByUserId")
    Result updatePhoneByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("修改头像")
    @PostMapping("/user/updateAvatorByUserId")
    Result updateAvatorByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("查询用户的数据权限")
    @PostMapping("/user/findUserDataPermissionList")
    Result findUserDataPermissionList(@RequestBody UserIdAndCompanyIdDTO dto);

    @ApiOperation("修改用户")
    @PostMapping("/user/updateUser")
    Result updateUser(@RequestBody User user);

}
