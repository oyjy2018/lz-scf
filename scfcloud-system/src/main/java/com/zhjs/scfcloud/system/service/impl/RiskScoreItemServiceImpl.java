package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ScoreItemDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttr;
import com.zhjs.scfcloud.model.entity.RiskControlModel;
import com.zhjs.scfcloud.model.entity.RiskScoreItems;
import com.zhjs.scfcloud.model.entity.ScoreItemsFormula;
import com.zhjs.scfcloud.model.mapper.BusinessAttrMapper;
import com.zhjs.scfcloud.model.mapper.RiskControlModelMapper;
import com.zhjs.scfcloud.model.mapper.RiskScoreItemsMapper;
import com.zhjs.scfcloud.model.mapper.ScoreItemsFormulaMapper;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.ScoreItemEditVO;
import com.zhjs.scfcloud.model.vo.ScoreItemVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.risk.RiskControlVariableVO;
import com.zhjs.scfcloud.system.service.RiskScoreItemService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RiskScoreItemServiceImpl extends ServiceImpl<RiskScoreItemsMapper, RiskScoreItems> implements RiskScoreItemService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessAttrMapper businessAttrMapper;
    @Autowired
    private RiskControlModelMapper riskControlModelMapper;
    @Autowired
    private ScoreItemsFormulaMapper scoreItemsFormulaMapper;

    @Override
    public Map<String, Object> getScoreItemVariable(Long controlModelId) {
        Map<String, Object> result = new HashMap<>();
        RiskControlModel controlModel = riskControlModelMapper.selectById(controlModelId);
        //业务流程中，用户在申请单填写的字段
        QueryWrapper<BusinessAttr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("business_type_id",controlModel.getBusinessTypeId());
        attrQueryWrapper.eq("company_id", controlModel.getCompanyId());
        attrQueryWrapper.eq("has_create_visible", CommonEnum.WhetherEnum.YES.getStatus());
        List<BusinessAttr> attrList = businessAttrMapper.selectList(attrQueryWrapper);
        List<RiskControlVariableVO> applyList = new ArrayList<>();
        attrList.forEach(businessAttr -> {
            RiskControlVariableVO variableVO = new RiskControlVariableVO();
            variableVO.setColumnCh(businessAttr.getColumnCh());
            variableVO.setColumnType(businessAttr.getColumnType());
            variableVO.setColumnName("applyFiled-"+businessAttr.getColumnName());
            applyList.add(variableVO);
        });
        result.put("applyFiled",applyList);
        //业务流程中，审核过程填写的字段
        List<BusinessWorkFlowExtendVO> workFlowCfgList = BusinessCfgUtil.getBusinessWorkFlowExtendList(controlModel.getCompanyId(),controlModel.getBusinessTypeId());
        List<RiskControlVariableVO> audditAttrList = new ArrayList<>();
        workFlowCfgList.forEach(businessWorkFlowExtendVO -> {
            if(businessWorkFlowExtendVO.getBusinessAttrCfgVOList() != null){
                businessWorkFlowExtendVO.getBusinessAttrCfgVOList().forEach(businessAttrCfgVO -> {
                    RiskControlVariableVO variableVO = new RiskControlVariableVO();
                    variableVO.setColumnCh(businessAttrCfgVO.getColumnCh());
                    variableVO.setColumnType(businessAttrCfgVO.getColumnType());
                    variableVO.setColumnName("reviewFiled-"+businessAttrCfgVO.getColumnName());
                    audditAttrList.add(variableVO);
                });
            }
        });
        result.put("reviewFiled",audditAttrList);
        //深华工程系统-项目数据
        result.put("projectData",null);
        return result;
    }

    @Override
    public List<RiskScoreItems> findList(Long controlModelId) {
        QueryWrapper<RiskScoreItems> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.eq("risk_control_model_id",controlModelId);
        itemsQueryWrapper.eq("status", CommonEnum.WhetherEnum.YES.getStatus());
        itemsQueryWrapper.orderByDesc("update_time");
        return this.list(itemsQueryWrapper);
    }

    @Override
    public Result findDetail(Long riskScoreItemId) {
        RiskScoreItems record = this.getById(riskScoreItemId);
        if(record == null) return Result.failure("当前评分项不存在");
        ScoreItemDTO dto = new ScoreItemDTO();
        BeanUtils.copyProperties(record, dto);

        QueryWrapper<ScoreItemsFormula> formulaQueryWrapper = new QueryWrapper<>();
        formulaQueryWrapper.eq("scoring_items_id", riskScoreItemId);
        dto.setFormulaList(scoreItemsFormulaMapper.selectList(formulaQueryWrapper));
        return Result.success(dto);
    }

    @Override
    @Transactional
    public Result addScoreItem(ScoreItemVO scoreItemVO) {
        QueryWrapper<RiskScoreItems> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.eq("item_name", scoreItemVO.getItemName());
        itemsQueryWrapper.eq("status", CommonEnum.WhetherEnum.YES.getStatus());
        if(this.count(itemsQueryWrapper) > 0){
            return Result.failure("评分项名称已存在");
        }
        RiskScoreItems record = new RiskScoreItems();
        BeanUtils.copyProperties(scoreItemVO,record);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        record.setStatus(CommonEnum.WhetherEnum.YES.getStatus());
        this.save(record);

        scoreItemVO.getFormulaList().forEach(scoreItemFormulaVO -> {
            ScoreItemsFormula formula = new ScoreItemsFormula();
            BeanUtils.copyProperties(scoreItemFormulaVO, formula);

            formula.setScoringItemsId(record.getId());
            formula.setStatus(CommonEnum.WhetherEnum.YES.getStatus());
            formula.setCreateTime(new Date());
            formula.setUpdateTime(new Date());
            scoreItemsFormulaMapper.insert(formula);
        });
        //修改总公式
        this.updateById(record);

        return Result.success();
    }

    @Override
    @Transactional
    public Result updateScoreItem(ScoreItemEditVO editVO) {
        RiskScoreItems record = this.getById(editVO.getScoreItemId());
        if(record == null) return Result.failure("当前评分项不存在");
        if(!StringUtils.isEmpty(record.getItemName()) &&
                !editVO.getItemName().equals(record.getItemName())){
            QueryWrapper<RiskScoreItems> itemsQueryWrapper = new QueryWrapper<>();
            itemsQueryWrapper.eq("item_name", editVO.getItemName());
            if(this.count(itemsQueryWrapper) > 0){
                return Result.failure("评分项名称已存在");
            }
        }

        //删除所有评分项公式
        QueryWrapper<ScoreItemsFormula> formulaQueryWrapper = new QueryWrapper<>();
        formulaQueryWrapper.eq("scoring_items_id", editVO.getScoreItemId());
        scoreItemsFormulaMapper.delete(formulaQueryWrapper);

        //新增评分项公式
        editVO.getFormulaList().forEach( scoreItemFormulaEditVO -> {
            ScoreItemsFormula formula = new ScoreItemsFormula();
            BeanUtils.copyProperties(scoreItemFormulaEditVO, formula);

            formula.setScoringItemsId(record.getId());
            formula.setStatus(CommonEnum.WhetherEnum.YES.getStatus());
            formula.setCreateTime(new Date());
            formula.setUpdateTime(new Date());
            scoreItemsFormulaMapper.insert(formula);
        });
        //修改评分项
        BeanUtils.copyProperties(editVO, record);
        record.setUpdateTime(new Date());

        this.updateById(record);
        return Result.success();
    }

    @Override
    public Result updateFullMarkById(BigDecimal fullMark, Long riskScoreItemId) {
        RiskScoreItems record = this.getById(riskScoreItemId);
        if(record == null) return Result.failure("当前评分项不存在");

        record.setFullMark(fullMark);
        record.setUpdateTime(new Date());
        this.updateById(record);
        return Result.success();
    }

    @Override
    public Result delelteScoreById(Long riskScoreItemId) {
        RiskScoreItems record = this.getById(riskScoreItemId);
        if(record == null) return Result.failure("当前评分项不存在");

        record.setStatus(CommonEnum.WhetherEnum.NO.getStatus());
        record.setUpdateTime(new Date());
        this.updateById(record);
        return Result.success();
    }
}
