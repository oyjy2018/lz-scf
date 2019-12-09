package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import com.zhjs.scfcloud.system.service.SmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 15:04
 *
 * @author liuchanghai
 * @create 2019-05-22 15:04
 * @since
 */

@Api(tags = "验证码管理")
@RestController
@RequestMapping("smscode")
public class SmsCodeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmsCodeService smsCodeService;

    @ApiOperation("获取手机短信验证码")
    @PostMapping("getPhoneVCode")
    public Result getPhoneVCode(@RequestBody PhoneVaildCodeDTO dto){
        logger.info(dto.toString());
        return smsCodeService.sendSms(dto);
    }

    @ApiOperation("手机验证码校验")
    @PostMapping("phoneCheck")
    public Result phoneCheck(@RequestBody CheckPhoneVCodeDTO dto){
        logger.info(dto.toString());
        boolean result = smsCodeService.checkVCode(dto);
        if(result){
            return Result.success("校验通过");
        }else {
            return Result.failure("验证码不正确");
        }
    }

    @ApiOperation("获取邮箱验证码")
    @PostMapping("getEmailVCode")
    public Result getEmailVCode(@RequestBody EmailVCodeDTO dto){
        logger.info(dto.toString());
        return smsCodeService.sendEmailVCode(dto.getEmail(),dto.getUserName());
    }

    @ApiOperation("邮箱验证码校验")
    @PostMapping("emailVCodeCheck")
    public Result emailVCodeCheck(@RequestBody EmailVCodeDTO dto){
        logger.info(dto.toString());
        return smsCodeService.emailVCodeCheck(dto.getEmail(), dto.getVcode());
    }
}
