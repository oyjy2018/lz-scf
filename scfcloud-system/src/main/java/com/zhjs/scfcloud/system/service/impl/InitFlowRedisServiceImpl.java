package com.zhjs.scfcloud.system.service.impl;

import com.zhjs.scfcloud.system.feign.RedisFeignService;
import com.zhjs.scfcloud.system.service.InitFlowRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InitFlowRedisServiceImpl implements InitFlowRedisService {
    private static final Logger logger = LoggerFactory.getLogger(InitFlowRedisServiceImpl.class);
    @Resource
    RedisFeignService redisFeignService;

    @Override
    @Async
    public void initFlowRedis() {
        logger.info("初始化Redis缓存-start");
        try {
            redisFeignService.initFlowRedis();
        }catch (Exception e){
            logger.info("初始化Redis缓存失败："+ e.getMessage());
            return;
        }
        logger.info("初始化Redis缓存-end");
    }

    @Override
    public void initFlowRedisNoAsync() {
        logger.info("初始化Redis缓存-start");
        try {
            redisFeignService.initFlowRedis();
        }catch (Exception e){
            logger.info("初始化Redis缓存失败："+ e.getMessage());
            return;
        }
        logger.info("初始化Redis缓存-end");
    }
}
