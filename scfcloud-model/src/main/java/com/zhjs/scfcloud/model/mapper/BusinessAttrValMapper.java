package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;

/**
 * 业务属性字段值配置 Mapper 接口
 * @author:dailongting
 * @date:2019-06-20 20:54
 */
public interface BusinessAttrValMapper extends BaseMapper<BusinessAttrVal> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessAttrVal record);

    int insertSelective(BusinessAttrVal record);

    BusinessAttrVal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessAttrVal record);

    int updateByPrimaryKey(BusinessAttrVal record);
}