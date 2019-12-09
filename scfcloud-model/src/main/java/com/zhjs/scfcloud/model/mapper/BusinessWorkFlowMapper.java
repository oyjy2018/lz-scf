package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.BusinessWorkFlow;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;

import java.util.List;

/**
 * 业务流程扭转表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-15 14:33
 */
public interface BusinessWorkFlowMapper extends BaseMapper<BusinessWorkFlow> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessWorkFlow record);

    int batchInsert(List<BusinessWorkFlow> list);

    int insertSelective(BusinessWorkFlow record);

    BusinessWorkFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessWorkFlow record);

    int updateByPrimaryKey(BusinessWorkFlow record);

    //查询流转流程以及流转条件
    List<BusinessWorkFlowExtendVO> getBusinessWorkFlowAndPermitList();
}