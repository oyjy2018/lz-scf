package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 菜单管理控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 15:48
 *
 * @author liuchanghai
 * @create 2019-05-17 15:48
 * @since
 */

@Api(tags = "菜单管理")
@RestController
@RequestMapping("menu")
public class MenuController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuService menuService;

    @ApiOperation("新建菜单")
    @PostMapping("add")
    public Result add(@RequestBody AddMenuDTO dto) {
        logger.info(dto.toString());
        boolean result = menuService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("是否存在菜单")
    @PostMapping("isExist")
    public Result isExist(@Valid @RequestBody IsExistMenuDTO dto) {
        logger.info(dto.toString());
//        menuService.isExist();
//        if(result){
//            return Result.success();
//        }
        return Result.failure();
    }

    @ApiOperation("编辑菜单")
    @PostMapping("edit")
    public Result edit(@RequestBody EditMenuDTO dto) {
        logger.info(dto.toString());
        boolean result = menuService.edit(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除菜单")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        boolean result = menuService.removeById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询菜单列表")
    @PostMapping("find/list")
    public Result findList(@RequestBody MenuListDTO dto) {
        logger.info(dto.toString());
        return Result.success();
    }
}
