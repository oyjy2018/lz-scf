package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.model.dto.PhoneVaildCodeDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: dailongting
 * @date:2019/5/24 15:26
 */
@FeignClient(value = "scfcloud-system", contextId = "api")
public interface  ApiFeignService {

    @ApiOperation("检查手机号是否存在")
    @PostMapping("/user/phoneCheck")
    Result phoneCheck(@RequestParam("phone") String phone);

    @ApiOperation("检查邮箱是否存在")
    @PostMapping("/user/emailCheck")
    Result emailCheck(@RequestParam("email") String email);

    @ApiOperation("获取手机短信验证码")
    @PostMapping("/smscode/getPhoneVCode")
    Result getPhoneVCode(@RequestBody PhoneVaildCodeDTO dto);

    @ApiOperation("手机验证码校验")
    @PostMapping("/smscode/phoneCheck")
    Result getVCodeCheck(@RequestBody CheckPhoneVCodeDTO dto);

    @ApiOperation("获取邮箱验证码")
    @PostMapping("/smscode/getEmailVCode")
    Result getEmailVCode(@RequestBody EmailVCodeDTO dto);

    @ApiOperation("邮箱验证码校验")
    @PostMapping("/smscode/emailVCodeCheck")
    Result emailVCodeCheck(@RequestBody EmailVCodeDTO dto);

}
