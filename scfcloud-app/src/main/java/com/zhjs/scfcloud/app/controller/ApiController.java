package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.ApiFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-20 13:35
 *
 * @author liuchanghai
 * @create 2019-06-20 13:35
 * @since
 */

@RestController
@RequestMapping("/api/")
public class ApiController {

    private Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private ApiFeignService apiFeignService;

    @ApiOperation("获取手机短信验证码")
    @PostMapping("getPhoneVCode")
    public Result getPhoneVCode(@RequestBody PhoneVaildCodeDTO dto) {
        return apiFeignService.getPhoneVCode(dto);
    }

    @ApiOperation("手机验证码校验")
    @PostMapping("phoneVCodeCheck")
    public Result getVCodeCheck(@RequestBody CheckPhoneVCodeDTO dto) {
        return apiFeignService.getVCodeCheck(dto);
    }

    @ApiOperation("获取邮箱验证码")
    @PostMapping("getEmailVCode")
    public Result getEmailVCode(@RequestBody EmailVCodeDTO dto) {
        return apiFeignService.getEmailVCode(dto);
    }

    @ApiOperation("邮箱验证码校验")
    @PostMapping("emailVCodeCheck")
    public Result emailVCodeCheck(@RequestBody EmailVCodeDTO dto) {
        return apiFeignService.emailVCodeCheck(dto);
    }
}
