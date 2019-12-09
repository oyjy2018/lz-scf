package com.zhjs.scfcloud.task.config;

import com.zhjs.scfcloud.task.init.InitRedis;
import com.zhjs.scfcloud.task.init.InitQuartz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动时执行
 **/
@Component
@Order(10)
public class ScfCloudApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ScfCloudApplicationRunner.class);

    @Autowired
    InitRedis initRedis;
    @Autowired
    InitQuartz initQuartz;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("---------------------开始初始化任务--------------------");
        initRedis.initRedis();
        initQuartz.initQuartz();
        logger.info("---------------------结束初始化任务--------------------");
    }
}
