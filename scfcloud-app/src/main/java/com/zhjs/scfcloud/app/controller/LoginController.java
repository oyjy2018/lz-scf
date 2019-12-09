package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.UserFeignService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.UserLoginDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * App 登录管理 控制器
 * @author: dailongting
 * @date:2019/6/17 11:40
 */

@RestController
@RequestMapping("/")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserFeignService userFeignService;

    @PostMapping("login")
    public Result login(@RequestBody UserLoginDTO dto) {
        logger.info("登录参数：{}", dto.toString());
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(dto.getLoginAccount(), dto.getPassword());
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);
                //这里主要是为了主动触发用户的认证，以此达到登录即初始化用户的权限信息
                subject.hasRole("admin");
            } catch (AuthenticationException e) {
                logger.error("用户名或密码错误:{}", e.getMessage());
                return Result.failure("用户名或密码错误");
            }
            User user = MySubjectUtil.getUser();
            //是否在职
//            if (user.getCompanyUserStatus() != UserEnum.CompanyUsrStatus.company_user_status_1.getStatus()){
//                return Result.failure("您尚未加入任何公司，请联系管理员邀请您加入");
//            }
//            //公司状态
//            if (user.getCompanyStatus() == CompanyStatusEnum.status0.getValue()){
//                return Result.failure("公司待审核");
//            }
//            if (user.getCompanyStatus() == CompanyStatusEnum.status3.getValue()){
//                return Result.failure("公司已禁用");
//            }
            //这里的代码顺序不能变
            Map<String, String> loginResult = new HashMap<>();
            loginResult.put("userName", user.getUserName());
            loginResult.put("token", subject.getSession().getId().toString());
            loginResult.put("userId", MySubjectUtil.getUserId().toString());
            loginResult.put("companyId", MySubjectUtil.getCompanyId().toString());
            loginResult.put("companyName", MySubjectUtil.getCompanyName());
            loginResult.put("email", user.getEmail());
            return Result.success(loginResult);
        } catch (Exception e) {
            logger.error("登录异常：{}", e.getMessage());
            return Result.failure("登录异常：{}", e.getMessage());
        }
    }

    /**
     * 忘记密码
     * @return
     */
    @PostMapping("updatePassword")
    public Result forgotPassword(@RequestBody Map<String,String> dto){
        logger.info("登录异常：{}", dto.toString());
        if (StringUtils.isEmpty(dto.get("phone"))){
            return Result.failure("手机号不能为空");
        }
        if (!StringUtil.isPhone(dto.get("phone"))){
            Result.failure("手机号格式错误");
        }
        if (!StringUtil.isEmpty(dto.get("password"))){
            Result.failure("密码不能为空");
        }
        if (!StringUtil.matchPwdReg(dto.get("password"))){
            Result.failure("密码必须包含大小写字母、数字、符号中至少3种");
        }
        return userFeignService.updatePassword(dto.get("phone"), dto.get("password"));
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        try{
            SecurityUtils.getSubject().logout();
            return Result.success("退出登录成功");
        }catch (Exception e){
            logger.error("退出登录异常：{}", e.getMessage());
            return Result.failure("退出登录异常");
        }
    }
}
