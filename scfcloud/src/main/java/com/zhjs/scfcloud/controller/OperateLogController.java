package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.OperateLogFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-28 16:42
 *
 * @author liuchanghai
 * @create 2019-06-28 16:42
 * @since
 */
public class OperateLogController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OperateLogFeignService operateLogFeignService;

    @ApiOperation("查询操作日志列表")
    @PostMapping("findList")
    public String findList(@RequestBody OperateLogDTO dto) {
        logger.info("查询操作日志列表");
        return operateLogFeignService.findList(dto);
    }
}
