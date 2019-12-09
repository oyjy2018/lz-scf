package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessFlow;

/**
 * 业务流程状态表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-20 20:40
 */
public interface BusinessFlowMapper extends BaseMapper<BusinessFlow> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessFlow record);

    int insertSelective(BusinessFlow record);

    BusinessFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessFlow record);

    int updateByPrimaryKey(BusinessFlow record);
}