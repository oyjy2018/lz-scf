package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.entity.OperateLog;
import com.zhjs.scfcloud.model.vo.OperateLogVO;
import com.zhjs.scfcloud.system.service.OperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-21 16:24
 *
 * @author liuchanghai
 * @create 2019-06-21 16:24
 * @since
 */

@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/operateLog/")
public class OperateLogController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OperateLogService operateLogService;

    @ApiOperation("查询操作日志列表")
    @PostMapping("findList")
    public String findList(@RequestBody OperateLogDTO dto) {
        logger.info("查询操作日志列表");
        return operateLogService.findList(dto);
    }

    @ApiOperation("新建操作日志")
    @PostMapping("add")
    public Result add(@RequestBody OperateLog dto) {
        logger.info("新建操作日志");
        if(operateLogService.save(dto)){
            return Result.success();
        }
        return Result.failure();
    }
}
