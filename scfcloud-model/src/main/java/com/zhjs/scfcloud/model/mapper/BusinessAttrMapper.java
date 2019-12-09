package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessAttr;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;

import java.util.List;
import java.util.Map;

/**
 * 业务属性配置表(表单配置) Mapper 接口
 * @author:dailongting
 * @date:2019-06-12 15:47
 */
public interface BusinessAttrMapper extends BaseMapper<BusinessAttr> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessAttr record);

    int insertSelective(BusinessAttr record);

    BusinessAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessAttr record);

    int updateByPrimaryKey(BusinessAttr record);

    /**
     * 查询业务属性配置
     * @return
     */
    List<BusinessAttrCfgVO> findBusinessAttrCfg();

    /**
     * 将字段值保存到指定表
     * @return
     */
    int insertColumnByTableName(Map<String, Object> params);

    /**
     * 更新指定表的字段值
     * @param params
     * @return
     */
    int updateColumnByTableName(Map<String, Object> params);
}