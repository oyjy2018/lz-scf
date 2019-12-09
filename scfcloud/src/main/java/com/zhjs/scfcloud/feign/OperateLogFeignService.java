package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-28 16:45
 *
 * @author liuchanghai
 * @create 2019-06-28 16:45
 * @since
 */
@FeignClient(value = "scfcloud-system", contextId = "operateLog")
public interface OperateLogFeignService {

    @ApiOperation("查询操作日志列表")
    @PostMapping("/operateLog/findList")
    String findList(@RequestBody OperateLogDTO dto);
}
