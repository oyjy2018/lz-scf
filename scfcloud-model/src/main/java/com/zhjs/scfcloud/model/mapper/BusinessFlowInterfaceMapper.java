package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessFlowInterface;

public interface BusinessFlowInterfaceMapper extends BaseMapper<BusinessFlowInterface> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessFlowInterface record);

    int insertSelective(BusinessFlowInterface record);

    BusinessFlowInterface selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessFlowInterface record);

    int updateByPrimaryKey(BusinessFlowInterface record);
}