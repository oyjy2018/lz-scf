package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 公司银行账户
 */

@FeignClient(value = "scfcloud-system", contextId = "companyUser")
public interface CompanyUserFeignService {

    @ApiOperation("个人信息")
    @GetMapping("/company/user/{userId}")
    Result userInfo(@PathVariable Long userId);

    @ApiOperation("修改个人信息")
    @PostMapping("/company/user/{userId}")
    Result updateInfo(@PathVariable Long userId,@RequestBody CompanyUserInfoEditDTO companyUserInfoEditDTO);

    @ApiOperation("离职")
    @PostMapping("/company/user/{userId}/leave")
    Result leave(@PathVariable Long userId);

    @ApiOperation("校验用户密码")
    @PostMapping("/company/user/{userId}/checkPassword")
    Result checkPassword(@PathVariable Long userId,@RequestBody CompanyUserPasswordDTO companyUserPasswordDTO);

    @ApiOperation("修改用户邮箱")
    @PostMapping("/company/user/{userId}/email")
    Result updateUserEmail(@PathVariable Long userId,@RequestBody CompanyUserEmailEditDTO companyUserEmailEditDTO);

    @ApiOperation("修改用户手机")
    @PostMapping("/company/user/{userId}/phone")
    Result updateUserPhone(@PathVariable Long userId,@RequestBody CompanyUserPhoneEditDTO companyUserPhoneEditDTO);

    @ApiOperation("修改账户密码")
    @PostMapping("/company/user/{userId}/password")
    Result updatePassword(@PathVariable Long userId,@RequestBody CompanyUserPasswordEditDTO companyUserPasswordEditDTO);

}
