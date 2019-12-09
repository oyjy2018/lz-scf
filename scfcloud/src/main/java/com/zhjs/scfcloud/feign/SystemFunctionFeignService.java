package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 系统功能管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 11:16
 *
 * @author liuchanghai
 * @create 2019-05-31 11:16
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "system-function")
public interface SystemFunctionFeignService {

    @ApiOperation("查询系统版本-功能列表")
    @PostMapping("/system/{systemId}/company/{companyId}/function")
    Result findSystemFunctions(@PathVariable Long systemId, @PathVariable Long companyId);
}
