package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.model.entity.RiskControlModel;
import com.zhjs.scfcloud.model.entity.RiskScoreItems;
import com.zhjs.scfcloud.model.mapper.RiskControlModelMapper;
import com.zhjs.scfcloud.model.mapper.RiskScoreItemsMapper;
import com.zhjs.scfcloud.model.transfer.RiskControlModelTransfer;
import com.zhjs.scfcloud.system.service.RiskControlModelService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *风控模型
 */

@Service
public class RiskControlModelServiceImpl extends ServiceImpl<RiskControlModelMapper, RiskControlModel> implements RiskControlModelService {

    private Logger logger = LoggerFactory.getLogger(RiskControlModelServiceImpl.class);

    @Resource
    private RiskControlModelMapper riskControlModelMapper;
    @Resource
    private RiskControlModelTransfer riskControlModelTransfer;
    @Resource
    private RiskScoreItemsMapper riskScoreItemsMapper;


    //模型列表
    @Override
    public Result list(RiskControlModelListDTO dto) {
        dto.setRecords(riskControlModelMapper.list(dto));
        return Result.successPage(dto);
    }

    //启用/禁用风控模型（修改风控模型启用状态）
    @Override
    public Result updateHasEnabled(BaseSingleUpdateDTO dto) {
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(dto.getId());
        if (riskControlModel == null) return Result.failure("风控模型不存在");
        riskControlModel.setHasEnabled(Integer.parseInt(dto.getValue()));
        riskControlModel.setUpdateBy(dto.getUpdateBy());
        riskControlModel.setUpdateTime(new Date());
        if (riskControlModelMapper.updateById(riskControlModel) != 1) {
            return Result.failure("修改启用状态失败");
        }
        return Result.success();
    }

    //删除风控模型
    @Override
    public Result delete(BaseDeleteDTO dto) {
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(dto.getId());
        if (riskControlModel == null) return Result.failure("风控模型不存在");
        riskControlModel.setStatus(CommonEnum.WhetherEnum.NO.getStatus()); //软删状态
        riskControlModel.setUpdateBy(dto.getUserId());
        riskControlModel.setUpdateTime(new Date());
        if (riskControlModelMapper.updateById(riskControlModel) != 1) {
            return Result.failure("删除失败");
        }
        return Result.success();
    }

    //添加风控模型
    @Override
    public Result insert(RiskControlModelInsertDTO dto) {
        String modelName = StringUtil.trimAll(dto.getModelName());
        //同一家公司中，模型名称不能重复
        LambdaQueryWrapper<RiskControlModel> wrapper = new QueryWrapper<RiskControlModel>().lambda();
        wrapper.eq(RiskControlModel::getCompanyId,dto.getCompanyId());
        wrapper.eq(RiskControlModel::getModelName,modelName);
        wrapper.eq(RiskControlModel::getStatus,CommonEnum.WhetherEnum.YES.getStatus());
        int existCount = riskControlModelMapper.selectCount(wrapper);
        if (existCount > 0) return Result.failure("模型名称重复，请重新输入");
        //转换
        RiskControlModel riskControlModel = riskControlModelTransfer.toRiskControlModel(dto);
        riskControlModel.setModelName(modelName);
        riskControlModel.setStatus(CommonEnum.WhetherEnum.YES.getStatus());
        riskControlModel.setHasEnabled(CommonEnum.WhetherEnum.YES.getStatus());
        riskControlModel.setCreateBy(dto.getUserId());
        riskControlModel.setCreateTime(new Date());
        if (riskControlModelMapper.insert(riskControlModel) != 1) {
            return Result.failure("新增失败");
        }
        return Result.successData(riskControlModel.getId());
    }

    //修改风控模型
    @Override
    public Result update(RiskControlModelUpdateDTO dto) {
        String modelName = StringUtil.trimAll(dto.getModelName());
        //同一家公司中，模型名称不能重复
        LambdaQueryWrapper<RiskControlModel> wrapper = new QueryWrapper<RiskControlModel>().lambda();
        wrapper.eq(RiskControlModel::getModelName,modelName);
        wrapper.eq(RiskControlModel::getCompanyId,dto.getCompanyId());
        wrapper.eq(RiskControlModel::getStatus,CommonEnum.WhetherEnum.YES.getStatus());
        wrapper.ne(RiskControlModel::getId,dto.getId()); //取不是当前的模型
        int existCount = riskControlModelMapper.selectCount(wrapper);
        if (existCount > 0) return Result.failure("模型名称重复，请重新输入");
        //转换
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(dto.getId());
        riskControlModel.setModelName(modelName);
        riskControlModel.setSystemId(Long.parseLong(dto.getSystemId()));
        riskControlModel.setBusinessTypeId(Long.parseLong(dto.getBusinessTypeId()));
        riskControlModel.setBusinessTypeName(dto.getBusinessTypeName());
        riskControlModel.setModelExplain(dto.getModelExplain());
        riskControlModel.setUpdateBy(dto.getUserId());
        riskControlModel.setUpdateTime(new Date());
        if (riskControlModelMapper.updateById(riskControlModel) != 1) {
            return Result.failure("修改失败");
        }
        return Result.success();
    }

    //更新模型公式
    @Override
    public Result updateModelFormula(BaseSingleUpdateDTO dto) {
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(dto.getId());
        if (riskControlModel == null) return Result.failure("风控模型不存在");
        //获取该模型所有评分项
        LambdaQueryWrapper<RiskScoreItems> wrapper = new QueryWrapper<RiskScoreItems>().lambda();
        wrapper.eq(RiskScoreItems::getRiskControlModelId,dto.getId());
        List<RiskScoreItems> scoreItemsList = riskScoreItemsMapper.selectList(wrapper);
        if (ListUtil.isEmpty(scoreItemsList)) return Result.failure("风控模型无评分项");

        String modelFormula = dto.getValue();
        //将公式中的中文名称循环翻译为id
        for(RiskScoreItems riskScoreItems : scoreItemsList) {
            modelFormula = modelFormula.replace("["+riskScoreItems.getItemName()+"]","["+riskScoreItems.getId()+"]");
        }
        //判断中文评分项是否全部替换
        if (StringUtil.isContainChinese(modelFormula)) {
            return Result.failure("模型公式中含非法的评分项公式");
        }

        riskControlModel.setModelFormula(modelFormula);
        riskControlModel.setUpdateBy(dto.getUpdateBy());
        riskControlModel.setUpdateTime(new Date());
        if (riskControlModelMapper.updateById(riskControlModel) != 1) {
            return Result.failure("保存模型公式失败");
        }
        return Result.success();
    }

    //模型详情
    @Override
    public Result detail(Long id) {
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(id);
        return Result.success(riskControlModel);
    }

    //获取模型公式
    @Override
    public Result getModelFormula(Long id) {
        RiskControlModel riskControlModel = riskControlModelMapper.selectById(id);
        if (riskControlModel == null) return Result.failure("风控模型不存在");

        //获取该模型所有评分项
        LambdaQueryWrapper<RiskScoreItems> wrapper = new QueryWrapper<RiskScoreItems>().lambda();
        wrapper.eq(RiskScoreItems::getRiskControlModelId,id).eq(RiskScoreItems::getStatus, CommonEnum.WhetherEnum.YES.getStatus());
        List<RiskScoreItems> scoreItemsList = riskScoreItemsMapper.selectList(wrapper);

        Map retMap = new HashMap();
        retMap.put("modelFormula",null);
        retMap.put("modelFormulaCH",null);
        retMap.put("scoreItemsList",scoreItemsList);
        //获取公式
        String modelFormula = riskControlModel.getModelFormula();
        if (!StringUtil.isEmpty(modelFormula) && !ListUtil.isEmpty(scoreItemsList)) {
            String modelFormulaCH = modelFormula;
            //将公式中的id循环翻译为中文名称
            for(RiskScoreItems riskScoreItems : scoreItemsList) {
                modelFormulaCH = modelFormulaCH.replace("["+riskScoreItems.getId()+"]","["+riskScoreItems.getItemName()+"]");
            }
            retMap.put("modelFormula",modelFormula);
            retMap.put("modelFormulaCH",modelFormulaCH);
        }
        return Result.success(retMap);
    }
}
