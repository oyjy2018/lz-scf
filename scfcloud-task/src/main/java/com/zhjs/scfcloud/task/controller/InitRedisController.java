package com.zhjs.scfcloud.task.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.task.init.InitRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weijie.chen
 */
@RestController
public class InitRedisController {
    private static final Logger logger = LoggerFactory.getLogger(InitRedisController.class);

    @Autowired
    InitRedis initRedis;

    @PostMapping("/redis")
    public Result initRedis(){
        try {
            initRedis.initRedis();
        }catch (Exception e){
            return Result.failure("初始化redis失败："+e.getMessage());
        }
        return Result.success();
    }
}
