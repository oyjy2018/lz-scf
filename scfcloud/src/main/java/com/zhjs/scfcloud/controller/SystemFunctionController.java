package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.SystemFunctionFeignService;
import com.zhjs.scfcloud.model.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 11:14
 *
 * @author liuchanghai
 * @create 2019-05-31 11:14
 * @since
 */

@Api(tags = "系统管理")
@RestController
public class SystemFunctionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SystemFunctionFeignService systemFunctionFeignService;

    @ApiOperation("查询系统版本-功能列表")
    @GetMapping("/system/{systemId}/company/{companyId}/function")
    public Result findSystemFunctionByVersionId(@PathVariable Long systemId,@PathVariable Long companyId) {
        logger.info("查询系统版本-功能列表：systemId={},companyId={}",systemId,companyId);
        return systemFunctionFeignService.findSystemFunctions(systemId,companyId);
    }
}
