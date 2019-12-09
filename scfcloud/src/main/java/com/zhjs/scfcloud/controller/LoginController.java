package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.config.MySessionManager;
import com.zhjs.scfcloud.config.MyUsernamePasswordToken;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.UserLoginDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("home")
    public String home(){
        return "Hello Word";
    }

    @PostMapping("login")
    public String login(@RequestBody UserLoginDTO dto){
        logger.info("登录参数：{}", JsonUtil.toJSON(dto));
        try {
            String loginAccount  = new String(new BASE64Decoder().decodeBuffer(dto.getLoginAccount()));
            String password  = DigestUtils.md5DigestAsHex(new BASE64Decoder().decodeBuffer(dto.getPassword()));
            MyUsernamePasswordToken token = new MyUsernamePasswordToken(loginAccount, password);
            token.setCompanyId(dto.getCompanyId());
            Subject subject = SecurityUtils.getSubject();
            try{
                subject.login(token);
                //这里主要是为了主动触发用户的认证，以此达到登录即初始化用户的权限信息
                subject.hasRole("admin");
            }catch (AuthenticationException e){
                return Result.failure(e.getMessage()).toJSON();
            }
            //登录成功
            UserInfoVO user = MySubjectUtil.getUser();

            //这里的代码顺序不能变
            Map<String, Object> loginResult = new HashMap<>();
            loginResult.put("userName", user.getUserName());
            loginResult.put("companyName", user.getCompanyName());
            loginResult.put("roleNames", user.getRoleNames());
            loginResult.put("permissionType", user.getPermissionType());
            loginResult.put("permissionList", user.getSystemFunctionList().stream().map(fun -> {return fun.getFuncCode();}).collect(Collectors.joining(",")));
            loginResult.put("menuList",user.getMenuList().stream().collect(Collectors.joining(",")));
            loginResult.put(MySessionManager.AUTHENTICATIONTOKEN, subject.getSession().getId());
            return Result.success(loginResult).toJSON();
        }catch (Exception e){
            logger.error("登录异常：{}",e.getMessage());
            return Result.failure("登录异常：{}", e.getMessage()).toJSON();
        }
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public String logout(){
        try{
            SecurityUtils.getSubject().logout();
            return Result.success("退出登录成功").toJSON();
        }catch (Exception e){
            logger.error("退出登录异常：{}", e.getMessage());
            return Result.failure("退出登录异常").toJSON();
        }
    }
}
