package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.AddSystemVDTO;
import com.zhjs.scfcloud.model.dto.EditSystemVDTO;
import com.zhjs.scfcloud.system.service.SystemVersionService;
import com.zhjs.scfcloud.model.vo.SystemVVO;
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
 * 系统版本管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 10:45
 *
 * @author liuchanghai
 * @create 2019-05-23 10:45
 * @since
 */


@Api(tags = "系统功能管理")
@RestController
@RequestMapping("system/v")
public class SystemVersionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SystemVersionService systemVersionService;

    @ApiOperation("新建系统版本")
    @PostMapping("add")
    public Result add(@RequestBody AddSystemVDTO dto) {
        logger.info("新建系统版本 " + dto.toString());
        boolean result = systemVersionService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("编辑系统版本")
    @PostMapping("edit")
    public Result edit(@RequestBody EditSystemVDTO dto) {
        logger.info("编辑系统版本 " + dto.toString());
        boolean result = systemVersionService.edit(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除系统版本")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info("删除系统版本" + dto.toString());
        boolean result = systemVersionService.removeById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询系统版本列表")
    @PostMapping("find/list")
    public Result findList() {
        logger.info("查询系统版本列表 ");
        List<SystemVVO> list = systemVersionService.findList();
        return Result.success(list);
    }

    @ApiOperation("查询系统版本下拉列表")
    @PostMapping("select/list")
    public Result selectList() {
        logger.info("查询系统版本下拉列表 ");
        List<SystemVVO> list = systemVersionService.findList();
        return Result.success(list);
    }

}
