package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessFileCfg;

/**
 * 业务文件配置表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-21 15:33
 */
public interface BusinessFileCfgMapper extends BaseMapper<BusinessFileCfg> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessFileCfg record);

    int insertSelective(BusinessFileCfg record);

    BusinessFileCfg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessFileCfg record);

    int updateByPrimaryKey(BusinessFileCfg record);
}