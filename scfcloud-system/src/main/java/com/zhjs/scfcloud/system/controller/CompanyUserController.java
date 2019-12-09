package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.system.service.CompanyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author weijie.chen
 */
@Api(tags = "公司用户管理")
@RestController
public class CompanyUserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyUserService companyUserService;


    @ApiOperation("个人信息")
    @GetMapping("/company/user/{userId}")
    public Result userInfo(@PathVariable Long userId) {
        logger.info("查询个人信息，userID={}",userId);
        return Result.success(companyUserService.findCompanyUserInfo(userId));
    }

    @ApiOperation("修改个人信息")
    @PostMapping("/company/user/{userId}")
    public Result updateInfo(@PathVariable Long userId,@RequestBody CompanyUserInfoEditDTO companyUserInfoEditDTO) {
        logger.info("修改个人信息，userId:{},body:{}",userId,companyUserInfoEditDTO);
        try{
            companyUserService.updateCompanyUserInfo(userId,companyUserInfoEditDTO);
        }catch (Exception e){
            Result.failure("修改失败，失败原因：" + e.getMessage());
        }
        return Result.success();
    }

    @ApiOperation("离职")
    @PostMapping("/company/user/{userId}/leave")
    public Result leave(@PathVariable Long userId){
        logger.info("离职，userId:{}",userId);
        companyUserService.leave(userId);
        return Result.success();
    }

    @ApiOperation("校验用户密码")
    @PostMapping("/company/user/{userId}/checkPassword")
    public Result checkPassword(@PathVariable Long userId,@RequestBody CompanyUserPasswordDTO companyUserPasswordDTO) {
        logger.info("校验用户密码，userId:{},body:{}",userId,companyUserPasswordDTO);
        return companyUserService.checkCompanyUserPassword(userId,companyUserPasswordDTO.getPassword()) ? Result.success() : Result.failure("密码错误");
    }

    @ApiOperation("修改用户邮箱")
    @PostMapping("/company/user/{userId}/email")
    public Result updateUserEmail(@PathVariable Long userId,@RequestBody CompanyUserEmailEditDTO companyUserEmailEditDTO) {
        logger.info("修改用户邮箱，userId:{},body:{}",userId,companyUserEmailEditDTO);
        return companyUserService.updateUserEmail(userId,companyUserEmailEditDTO);
    }

    @ApiOperation("修改用户手机")
    @PostMapping("/company/user/{userId}/phone")
    public Result updateUserPhone(@PathVariable Long userId,@RequestBody CompanyUserPhoneEditDTO companyUserPhoneEditDTO) {
        logger.info("修改用户手机，userId:{},body:{}",userId,companyUserPhoneEditDTO);
        return companyUserService.updateUserPhone(userId,companyUserPhoneEditDTO);
    }

    @ApiOperation("修改密码")
    @PostMapping("/company/user/{userId}/password")
    public Result updatePassword(@PathVariable Long userId,@RequestBody CompanyUserPasswordEditDTO companyUserPasswordEditDTO) {
        logger.info("修改密码，userId:{},body:{}",userId,companyUserPasswordEditDTO);
        return companyUserService.updatePassword(userId,companyUserPasswordEditDTO);
    }

}
