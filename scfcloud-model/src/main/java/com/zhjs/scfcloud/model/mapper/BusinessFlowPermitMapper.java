package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.business.BusinessFlowPermitInitDTO;
import com.zhjs.scfcloud.model.entity.BusinessFlowPermit;

import java.util.List;

/**
 * 业务流程权限配置表 Mapper 接口
 * @author:oyjy
 * @date:2019-06-13 11:32
 */
public interface BusinessFlowPermitMapper extends BaseMapper<BusinessFlowPermit> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessFlowPermit record);

    int insertSelective(BusinessFlowPermit record);

    BusinessFlowPermit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessFlowPermit record);

    int updateByPrimaryKey(BusinessFlowPermit record);

    int updateDisposeRoleIdsBatch(List<BusinessFlowPermitInitDTO> flowPermitList);
}