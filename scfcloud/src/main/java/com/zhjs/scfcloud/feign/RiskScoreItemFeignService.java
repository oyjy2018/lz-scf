package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemFullMarkEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 公司银行账户
 */

@FeignClient(value = "scfcloud-system", contextId = "RiskScoreItem")
public interface RiskScoreItemFeignService {

    @ApiOperation("评分项公式参数")
    @GetMapping("/scoreItem/variable")
    Result getVariable(@RequestParam(required = true)Long modelId);

    @ApiOperation("评分项列表")
    @GetMapping("/scoreItem")
    Result list(@RequestParam(required = true)Long modelId);

    @ApiOperation("评分项详情")
    @GetMapping("/scoreItem/{riskScoreItemId}")
    Result detail(@PathVariable Long riskScoreItemId);

    @ApiOperation("添加评分项")
    @PostMapping("/scoreItem")
    Result add(@RequestBody ScoreItemVO scoreItemVO);

    @ApiOperation("修改评分项")
    @PutMapping(value = "/scoreItem")
    Result update(@RequestBody ScoreItemEditVO editVO);

    @ApiOperation("修改满分值")
    @PutMapping(value = "/scoreItem/{riskScoreItemId}")
    Result updateFullMark(@RequestBody ScoreItemFullMarkEditVO editVO, @PathVariable Long riskScoreItemId);

    @ApiOperation("删除评分项（逻辑删除）")
    @DeleteMapping(value = "/scoreItem/{riskScoreItemId}")
    Result delte(@PathVariable Long riskScoreItemId);
}
