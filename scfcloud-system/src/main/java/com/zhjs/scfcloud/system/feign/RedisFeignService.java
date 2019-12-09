package com.zhjs.scfcloud.system.feign;

import com.zhjs.scfcloud.model.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "scfcloud-task", contextId = "initRedis")
public interface RedisFeignService {

    @ApiOperation("初始化redis")
    @PostMapping("/redis")
    Result initFlowRedis();

}
