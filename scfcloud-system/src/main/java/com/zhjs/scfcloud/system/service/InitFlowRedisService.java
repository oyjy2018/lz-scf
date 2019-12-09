package com.zhjs.scfcloud.system.service;

public interface InitFlowRedisService {
    /**
     * 异步调用-初始化redis
     * @return
     */
    public void initFlowRedis();

    /**
     * 非异步调用-初始化redis
     * @return
     */
    public void initFlowRedisNoAsync();
}
