package com.zhjs.scfcloud.system.config;

import com.zhjs.scfcloud.model.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获类
 * @author: dailongting
 * @date:2019/7/2 17:52
 */
@ControllerAdvice
public class MyControllerAdvice {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 全局接口异常返回结果
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e){
        e.printStackTrace();
        logger.error("系统异常：{}",e.getMessage());
        return Result.exception("系统异常："+e.getMessage());
    }

}
