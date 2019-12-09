package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.ticket.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weijie.chen
 */
@Api("首页controller")
@RestController
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    IndexService indexService;

    @ApiOperation("首页")
    @GetMapping("/index")
    public Result index(){
        logger.info("首页加载.");
        return Result.success(indexService.index());
    }
}
