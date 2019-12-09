package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.OperateLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: dailongting
 * @date:2019/6/26 9:45
 */
@FeignClient(value = "scfcloud-system", contextId = "operateLog")
public interface OperateLogFeignService {
    @ApiOperation("新建操作日志")
    @PostMapping("/operateLog/add")
    Result add(@RequestBody OperateLog dto);
}
