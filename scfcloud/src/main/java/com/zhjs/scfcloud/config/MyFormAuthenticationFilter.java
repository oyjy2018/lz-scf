package com.zhjs.scfcloud.config;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private Logger log = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }

                return this.executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }

                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            //下面的方法会在请求不成功的情况下创建一个session保存到缓存，用postman测试接口，会每一次都创建一个新的session，
            //this.saveRequestAndRedirectToLogin(request, response);
            System.out.println("访问地址:"+getPathWithinApplication(request));
            returnJsonData(WebUtils.toHttp(response), Result.failure(Result.RESULT_CODE_LOGIN_INVALID,"未认证的请求"));
            return false;
        }
    }

    private void returnJsonData(HttpServletResponse response, Object obj){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JsonUtil.toJSON(obj));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
