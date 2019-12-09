package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessFeeFormula;

/**
 * 业务费用计算公式表 Mapper 接口
 * @author:oyjy
 * @date:2019-06-13 16:08
 */
public interface BusinessFeeFormulaMapper extends BaseMapper<BusinessFeeFormula> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessFeeFormula record);

    int insertSelective(BusinessFeeFormula record);

    BusinessFeeFormula selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessFeeFormula record);

    int updateByPrimaryKey(BusinessFeeFormula record);
}