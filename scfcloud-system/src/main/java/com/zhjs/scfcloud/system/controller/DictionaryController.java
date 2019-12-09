package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.vo.DictVO;
import com.zhjs.scfcloud.system.service.DictionaryService;
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
 * 数据字典管理控制器
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 17:28
 * @since
 */

@Api(tags = "数据字典管理")
@RestController
@RequestMapping("dict")
public class DictionaryController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation("新建行业数据字典")
    @PostMapping("add/nature")
    public Result addNature(@RequestBody AddNatureDTO dto) {
        logger.info(dto.toString());
        boolean result = dictionaryService.addNature(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("新建数据字典")
    @PostMapping("add")
    public Result add(@RequestBody AddDictDTO dto) {
        logger.info(dto.toString());
        boolean result = dictionaryService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("是否存在该键")
    @PostMapping("isExistKey")
    public Result isExistKey(@Valid @RequestBody IsExistDictDTO dto) {
        logger.info(dto.toString());
        boolean isExist = dictionaryService.isExist(dto);
        if(isExist){
            return Result.success();
        }
        return Result.failure();
    }


    @ApiOperation("编辑数据字典")
    @PostMapping("edit")
    public Result edit(@RequestBody EditDictDTO dto) {
        logger.info(dto.toString());
        boolean result = dictionaryService.edit(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除数据字典")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        boolean result = dictionaryService.removeById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询数据字典列表")
    @PostMapping("findList")
    public Result findList(@RequestBody DictListDTO dto) {
        logger.info(dto.toString());
        List<DictVO> list = dictionaryService.findList(dto);
        return Result.success(list);
    }
}
