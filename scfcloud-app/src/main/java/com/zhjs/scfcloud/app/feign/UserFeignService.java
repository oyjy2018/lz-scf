package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.UserIdAndCompanyIdDTO;
import com.zhjs.scfcloud.model.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @ApiOperation("修改密码")
    @PostMapping("/user/updatePassword")
    Result updatePassword(@RequestParam("phone") String phone, @RequestParam("newPassword") String newPassword);

    @ApiOperation("我的详情")
    @PostMapping("/user/myDetailsByUserId")
    Result myDetailsByUserId(@RequestBody BaseIdDTO dto);

    @ApiOperation("修改邮箱")
    @PostMapping("/user/updateEmailByUserId")
    Result updateEmailByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("修改手机号")
    @PostMapping("/user/updatePhoneByUserId")
    Result updatePhoneByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("修改密码")
    @PostMapping("/user/updatePasswordByUserId")
    Result updatePasswordByUserId(@RequestBody Map<String, Object> dto);

    @ApiOperation("修改头像")
    @PostMapping("/user/updateAvatorByUserId")
    Result updateAvatorByUserId(@RequestBody Map<String, String> dto);

    @ApiOperation("密码输入是否正确")
    @PostMapping("/user/isPassword")
    Result isPassword(@RequestBody Map<String, String> dto);

    @ApiOperation("查询用户加入的公司")
    @PostMapping("/company/findCompanyListByUserId")
    Result findCompanyListByUserId(@RequestBody UserIdAndCompanyIdDTO dto);

    @ApiOperation("修改用户")
    @PostMapping("/user/updateUser")
    Result updateUser(@RequestBody User user);
}
