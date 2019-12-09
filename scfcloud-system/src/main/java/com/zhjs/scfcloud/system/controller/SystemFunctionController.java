package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.SystemFunctionListDTO;
import com.zhjs.scfcloud.model.vo.SystemFunctionVO;
import com.zhjs.scfcloud.system.service.SystemFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统功能管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 11:03
 *
 * @author liuchanghai
 * @create 2019-05-20 11:03
 * @since
 */


@Api(tags = "系统功能管理")
@RestController
public class SystemFunctionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SystemFunctionService systemFunctionService;

    @ApiOperation("查询系统版本-功能列表")
    @PostMapping("/system/{systemId}/company/{companyId}/function")
    public Result findSystemFunctionByVersionId(@PathVariable Long systemId,@PathVariable Long companyId) {
        logger.info("查询系统版本-功能列表：systemId={},companyId={}",systemId,companyId);
        return Result.success(systemFunctionService.findSystemFunctions(systemId,companyId));
    }

    @ApiOperation("查询系统功能列表")
    @PostMapping("/system/function/find/list")
    public Result findList(@RequestBody SystemFunctionListDTO dto) {
        logger.info(dto.toString());
        List<SystemFunctionVO> list = systemFunctionService.findList(dto);
        return Result.success(list);
    }

}
