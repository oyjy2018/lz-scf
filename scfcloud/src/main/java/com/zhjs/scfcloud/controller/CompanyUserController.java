package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CompanyUserFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公司银行卡 Controller
 * @author weijie.chen
 */
@RestController
public class CompanyUserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyUserFeignService companyUserFeignService;

    @ApiOperation("个人信息")
    @GetMapping("/company/user")
//    @RequiresPermissions("common:companyUser:info")
    public Result userInfo(){
        logger.info("查询公司银行列表:{}");
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyUserFeignService.userInfo(user.getId());
    }

    @ApiOperation("修改个人信息")
    @PostMapping("/company/user")
//    @RequiresPermissions("common:companyUser:info")
    public Result updateInfo(@RequestBody @Validated CompanyUserInfoEditDTO companyUserInfoEditDTO, BindingResult bindingResult){
        logger.info("修改个人信息:{}", companyUserInfoEditDTO);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        return companyUserFeignService.updateInfo(user.getId(), companyUserInfoEditDTO);
    }

    @ApiOperation("离职")
    @PostMapping("/company/user/leave")
//    @RequiresPermissions("common:companyUser:leave")
    public Result leave(){
        logger.info("离职");
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        return companyUserFeignService.leave(user.getId());
    }

    @ApiOperation("校验用户密码")
    @PostMapping("/company/user/checkPassword")
//    @RequiresPermissions("common:companyUser:info")
    public Result checkPassword(@RequestBody @Validated CompanyUserPasswordDTO companyUserPasswordDTO, BindingResult bindingResult){
        logger.info("校验用户密码:{}", companyUserPasswordDTO);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        return companyUserFeignService.checkPassword(user.getId(), companyUserPasswordDTO);
    }

    @ApiOperation("修改用户邮箱")
    @PostMapping("/company/user/email")
//    @RequiresPermissions("common:companyUser:info")
    public Result updateUserEmail(@RequestBody @Validated CompanyUserEmailEditDTO companyUserEmailEditDTO, BindingResult bindingResult){
        logger.info("修改用户邮箱:{}", companyUserEmailEditDTO);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        return companyUserFeignService.updateUserEmail(user.getId(), companyUserEmailEditDTO);
    }

    @ApiOperation("修改用户手机")
    @PostMapping("/company/user/phone")
//    @RequiresPermissions("common:companyUser:info")
    public Result updateUserPhone(@RequestBody @Validated CompanyUserPhoneEditDTO companyUserPhoneEditDTO, BindingResult bindingResult){
        logger.info("修改用户手机:{}", companyUserPhoneEditDTO);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        return companyUserFeignService.updateUserPhone(user.getId(), companyUserPhoneEditDTO);
    }

    @ApiOperation("修改账户密码")
    @PostMapping("/company/user/password")
    public Result updatePassword(@RequestBody CompanyUserPasswordEditDTO companyUserPasswordEditDTO, BindingResult bindingResult){
        logger.info("修改账户密码:{}", companyUserPasswordEditDTO);
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        if(companyUserPasswordEditDTO.getNewPassword().equals(companyUserPasswordEditDTO.getOldPassword())){
            return Result.failure("新密码和原密码相同");
        }
        return companyUserFeignService.updatePassword(user.getId(), companyUserPasswordEditDTO);
    }

}
