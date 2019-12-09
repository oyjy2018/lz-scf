package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.system.service.RiskControlModelService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 风控模型相关
 */
@RestController
@RequestMapping("/risk/model/")
public class RiskControlModelController {
    private Logger logger = LoggerFactory.getLogger(RiskControlModelController.class);

    @Autowired
    private RiskControlModelService riskControlModelService;

    //获取风控模型列表
    @PostMapping("list")
    public Result list(@RequestBody RiskControlModelListDTO dto){
        logger.info("subject:{},dto:{}","获取风控模型列表", JsonUtil.toSerializerJSON(dto));

        return riskControlModelService.list(dto);
    }

    //启用/禁用风控模型（修改风控模型启用状态）
    @PostMapping("updateHasEnabled")
    public Result updateHasEnabled(@RequestBody BaseSingleUpdateDTO dto) {
        logger.info("subject:{},dto:{}","启用/禁用风控模型", JsonUtil.toSerializerJSON(dto));
        return riskControlModelService.updateHasEnabled(dto);
    }

    //删除风控模型
    @PostMapping("delete")
    public Result delete(@RequestBody BaseDeleteDTO dto) {
        logger.info("subject:{},dto:{}","删除风控模型", JsonUtil.toSerializerJSON(dto));
        return riskControlModelService.delete(dto);
    }

    //添加风控模型
    @PostMapping("insert")
    public Result insert(@RequestBody RiskControlModelInsertDTO dto) {
        logger.info("subject:{},dto:{}","添加风控模型", JsonUtil.toSerializerJSON(dto));
        return riskControlModelService.insert(dto);
    }

    //修改风控模型
    @PostMapping("update")
    public Result update(@RequestBody RiskControlModelUpdateDTO dto) {
        logger.info("subject:{},dto:{}","修改风控模型", JsonUtil.toSerializerJSON(dto));
        return riskControlModelService.update(dto);
    }

    //更新模型公式
    @PostMapping("updateModelFormula")
    public Result updateModelFormula(@RequestBody BaseSingleUpdateDTO dto) {
        logger.info("subject:{},dto:{}","更新模型公式", JsonUtil.toSerializerJSON(dto));
        return riskControlModelService.updateModelFormula(dto);
    }

    //查看模型详情
    @PostMapping("detail")
    public Result detail(@RequestParam("id") Long id) {
        logger.info("subject:{},id:{}","更新模型公式", id);
        return riskControlModelService.detail(id);
    }

    //获取模型公式
    @PostMapping("getModelFormula")
    public Result getModelFormula(@RequestParam("id") Long id) {
        logger.info("subject:{},id:{}","获取模型公式", id);
        return riskControlModelService.getModelFormula(id);
    }

}
