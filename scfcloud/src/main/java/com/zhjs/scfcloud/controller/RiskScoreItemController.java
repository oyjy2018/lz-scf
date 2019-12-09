package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.RiskScoreItemFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemFullMarkEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    RiskScoreItemFeignService riskScoreItemFeignService;

    @ApiOperation("评分项公式参数")
    @GetMapping(value = "/scoreItem/variable")
    public Result getVariable(@RequestParam(required = true)Long modelId){
        logger.info("查询评分项公式参数: modelId={}",modelId);
        return riskScoreItemFeignService.getVariable(modelId);
    }

    @ApiOperation("评分项列表-添加模型")
    @GetMapping(value = "/scoreItem")
    @RequiresPermissions("common:riskModel:insert")
    public Result addModelList(@RequestParam(required = true)Long modelId){
        logger.info("查询评分项列表：modelId={}",modelId);
        return riskScoreItemFeignService.list(modelId);
    }

    @ApiOperation("评分项列表-模型详情")
    @GetMapping(value = "/modelDetail/scoreItem")
    @RequiresPermissions("common:riskModel:detail")
    public Result detailModelList(@RequestParam(required = true)Long modelId){
        logger.info("查询评分项列表：modelId={}",modelId);
        return riskScoreItemFeignService.list(modelId);
    }

    @ApiOperation("评分项详情")
    @GetMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result detail(@PathVariable Long riskScoreItemId){
        logger.info("查询评分项详情：riskScoreItemId={}",riskScoreItemId);
        return riskScoreItemFeignService.detail(riskScoreItemId);
    }

    @ApiOperation("添加评分项")
    @PostMapping(value = "/scoreItem")
    public Result add(@RequestBody @Validated ScoreItemVO scoreItemVO, BindingResult bindingResult){
        logger.info("添加评分项：{}",scoreItemVO);
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return riskScoreItemFeignService.add(scoreItemVO);
    }

    @ApiOperation("修改评分项")
    @PutMapping(value = "/scoreItem")
    public Result update(@RequestBody @Validated ScoreItemEditVO editVO, BindingResult bindingResult){
        logger.info("修改评分项：{}",editVO);
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return riskScoreItemFeignService.update(editVO);
    }

    @ApiOperation("修改满分值")
    @PutMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result updateFullMark(@RequestBody ScoreItemFullMarkEditVO editVO,@PathVariable Long riskScoreItemId){
        logger.info("修改满分值：fullMark={},riskScoreItemId={}",editVO.getFullMark(),riskScoreItemId);
        BigDecimal fullMark = null;
        try{
            fullMark = new BigDecimal(editVO.getFullMark());
        }catch (Exception e){
            return Result.failure("请输入正确的数字格式："+ editVO.getFullMark());
        }
        if(new BigDecimal(1).compareTo(fullMark) == 1 || new BigDecimal(10000).compareTo(fullMark) == -1){
            return Result.failure("满分分值必须大于0或小于等于10000");
        }
        return riskScoreItemFeignService.updateFullMark(editVO, riskScoreItemId);
    }

    @ApiOperation("删除评分项（逻辑删除）")
    @DeleteMapping(value = "/scoreItem/{riskScoreItemId}")
    public Result delte(@PathVariable Long riskScoreItemId){
        logger.info("删除评分项：riskScoreItemId={}",riskScoreItemId);
        return riskScoreItemFeignService.delte(riskScoreItemId);
    }
}
