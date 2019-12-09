package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessAttrClassify;

public interface BusinessAttrClassifyMapper extends BaseMapper<BusinessAttrClassify> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessAttrClassify record);

    int insertSelective(BusinessAttrClassify record);

    BusinessAttrClassify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessAttrClassify record);

    int updateByPrimaryKey(BusinessAttrClassify record);
}