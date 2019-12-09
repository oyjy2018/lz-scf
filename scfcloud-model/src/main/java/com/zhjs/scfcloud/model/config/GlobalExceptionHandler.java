package com.zhjs.scfcloud.model.config;

import com.alibaba.fastjson.JSONException;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.exception.ScfRuntimeException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    private static final String logExceptionFormat = "异常: 类名: %s 异常详细信息: %s";
    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    //没有权限访问
    @ExceptionHandler(value = AuthorizationException.class)
    public String authorizationException(HttpServletRequest request, HttpServletResponse servletResponse, Exception e){
        logger.info("该角色没有访问权限");
        log.error(String.format(logExceptionFormat, e.getClass(), e.getMessage()),e);
        return Result.exception("该角色没有访问权限").toJSON();
    }

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public String runtimeException(RuntimeException ex) {
        //ex.printStackTrace();
        log.error(String.format(logExceptionFormat,  ex.getClass(), ex.getMessage()),ex);
        return Result.exception(Result.RESULT_CODE_EXCEPTION,"系统异常").toJSON();
    }

    //运行时异常
    @ExceptionHandler(ScfRuntimeException.class)
    public String scfRuntimeException(ScfRuntimeException ex) {
        //ex.printStackTrace();
        log.error(String.format(logExceptionFormat,  ex.getClass(), ex.getMessage()),ex);
        return Result.exception(Result.RESULT_CODE_EXCEPTION, ex.getMessage()).toJSON();
    }
    //类型转换异常
    @ExceptionHandler(JSONException.class)
    public String jsonException(JSONException ex) {
        //ex.printStackTrace();
        log.error(String.format(logExceptionFormat,  ex.getClass(), ex.getMessage()),ex);
        return Result.exception(Result.RESULT_CODE_EXCEPTION, "类型不正确").toJSON();
    }

    /**
     * 全局接口异常返回结果
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        log.error(String.format(logExceptionFormat,  e.getClass(), e.getMessage()),e);
        return Result.exception("系统异常："+e.getMessage());
    }
}

