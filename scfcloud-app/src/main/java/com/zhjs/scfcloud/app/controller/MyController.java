package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.ApiFeignService;
import com.zhjs.scfcloud.app.feign.UserFeignService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.CheckPhoneVCodeDTO;
import com.zhjs.scfcloud.model.dto.EmailVCodeDTO;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * App我的 管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-15 10:58
 *
 * @author liuchanghai
 * @create 2019-06-15 10:58
 * @since
 */

@RestController
@RequestMapping("/my/")
public class MyController {

    private Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    private ApiFeignService apiFeignService;
    @Autowired
    private UserFeignService userFeignService;

    @ApiOperation("我的详情")
    @PostMapping("myDetails")
    public Result myDetails() {
        // 获取userId
        BaseIdDTO dto = new BaseIdDTO();
        dto.setId(MySubjectUtil.getUserId());
        return userFeignService.myDetailsByUserId(dto);
    }

    @ApiOperation("修改邮箱")
    @PostMapping("updateEmail")
    public Result updateEmail(@RequestBody Map<String, String> dto) {
        if (!StringUtil.isPhone(dto.get("email"))){
            Result.failure("邮箱格式错误");
        }
        EmailVCodeDTO emailVCodeDTO = new EmailVCodeDTO();
        emailVCodeDTO.setEmail(dto.get("email"));
        emailVCodeDTO.setVcode(dto.get("vcode"));
        Result check = apiFeignService.emailVCodeCheck(emailVCodeDTO);
        if(check.getCode() != 10000){
            return check;
        }
        return userFeignService.updateEmailByUserId(dto);
    }

    @ApiOperation("修改手机号")
    @PostMapping("updatePhone")
    public Result updatePhone(@RequestBody Map<String, String> dto) {
        if (!StringUtil.isPhone(dto.get("phone"))){
            Result.failure("手机号格式错误");
        }
        CheckPhoneVCodeDTO phoneVCodeDTO = new CheckPhoneVCodeDTO();
        phoneVCodeDTO.setPhone(dto.get("phone"));
        phoneVCodeDTO.setVcode(dto.get("vcode"));
        Result check = apiFeignService.getVCodeCheck(phoneVCodeDTO);
        if(check.getCode() != 10000){
            return check;
        }
        return userFeignService.updatePhoneByUserId(dto);
    }

    @ApiOperation("修改密码")
    @PostMapping("updatePassword")
    public Result updatePassword(@RequestBody Map<String, Object> dto) {
        String newPassword = (String) dto.get("newPassword");
        if (!StringUtil.matchPwdReg(newPassword) || newPassword.length() < 8){
            Result.failure("密码至少8位，包含大小写字母、数字、符号中至少2种");
        }
        return userFeignService.updatePasswordByUserId(dto);
    }

    @ApiOperation("修改头像")
    @PostMapping("updateAvator")
    public Result updateAvator(@RequestBody Map<String, String> dto) {
        return userFeignService.updateAvatorByUserId(dto);
    }

    @ApiOperation("安全设置输入密码是否正确")
    @PostMapping("isPassword")
    public Result isPassword(@RequestBody Map<String, String> dto) {
        return userFeignService.isPassword(dto);
    }
}
