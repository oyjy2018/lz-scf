package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemFullMarkEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;
import com.zhjs.scfcloud.system.service.RiskScoreItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author  weijie.chen
 * @date 2019-10-16
 */
@Api(tags = "评分项管理")
@RestController
public class RiskScoreItemController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RiskScoreItemService riskScoreItemService;

    @ApiOperation("评分项公式参数")
    @GetMapping(value = "/scoreItem/variable")
    public Result getVariable(@RequestParam(required = true)Long modelId){
        logger.info("查询评分项公式参数：modelId={}",modelId);
        return Result.success(riskScoreItemService.getScoreItemVariable(modelId));
    }

    @ApiOperation("评分项列表")
    @GetMapping(value = "/scoreItem")
    public Result list(@RequestParam(required = true)Long modelId){
        logger.info("查询评分项列表：modelId={}",modelId);
        return Result.success(riskScoreItemService.findList(modelId));
    }

    @ApiOperation("评分项详情")
    @GetMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result detail(@PathVariable Long riskScoreItemId){
        logger.info("查询评分项详情：riskScoreItemId={}",riskScoreItemId);
        return riskScoreItemService.findDetail(riskScoreItemId);
    }

    @ApiOperation("添加评分项")
    @PostMapping(value = "/scoreItem")
    public Result add(@RequestBody ScoreItemVO scoreItemVO){
        logger.info("添加评分项：{}",scoreItemVO);
        return riskScoreItemService.addScoreItem(scoreItemVO);
    }

    @ApiOperation("修改评分项")
    @PutMapping(value = "/scoreItem")
    public Result update(@RequestBody ScoreItemEditVO editVO){
        logger.info("修改评分项：{}",editVO);
        return riskScoreItemService.updateScoreItem(editVO);
    }

    @ApiOperation("修改满分值")
    @PutMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result updateFullMark(@RequestBody ScoreItemFullMarkEditVO editVO, @PathVariable Long riskScoreItemId){
        logger.info("修改满分值：fullMark={},riskScoreItemId={}",editVO.getFullMark(),riskScoreItemId);
        return riskScoreItemService.updateFullMarkById(new BigDecimal(editVO.getFullMark()), riskScoreItemId);
    }

    @ApiOperation("删除评分项（逻辑删除）")
    @DeleteMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result delte(@PathVariable Long riskScoreItemId){
        logger.info("删除评分项：riskScoreItemId={}",riskScoreItemId);
        return riskScoreItemService.delelteScoreById(riskScoreItemId);
    }
}
