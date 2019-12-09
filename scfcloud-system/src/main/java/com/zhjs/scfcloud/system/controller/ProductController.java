package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.vo.ProductListVO;
import com.zhjs.scfcloud.system.service.ProductService;
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
import java.util.List;

/**
 * 产品管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 11:24
 *
 * @author liuchanghai
 * @create 2019-05-27 11:24
 * @since
 */


@Api(tags = "产品管理")
@RestController
@RequestMapping("product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductService productService;

    @ApiOperation("新建产品")
    @PostMapping("add")
    public Result add(@RequestBody AddProductDTO dto) {
        logger.info(dto.toString());
        boolean result = productService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("是否存在产品")
    @PostMapping("isExist")
    public Result isExistKey(@Valid @RequestBody IsExistProductDTO dto) {
        logger.info(dto.toString());
        boolean isExist = productService.isExist(dto);
        if(isExist){
            return Result.success();
        }
        return Result.failure();
    }


    @ApiOperation("编辑产品")
    @PostMapping("edit")
    public Result edit(@RequestBody EditProductDTO dto) {
        logger.info(dto.toString());
        boolean result = productService.edit(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除产品")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        boolean result = productService.removeById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询产品列表")
    @PostMapping("find/list")
    public Result findList(@RequestBody ProductListDTO dto) {
        logger.info(dto.toString());
        List<ProductListVO> list = productService.findList(dto);
        return Result.success(list);
    }

    @ApiOperation("查询产品下拉列表")
    @PostMapping("select/list")
    public Result selectList() {
        logger.info("查询产品下拉列表");
        List<ProductListVO> list = productService.findList(null);
        return Result.success(list);
    }
}
