package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.business.AddWorkFlowAttrDTO;
import com.zhjs.scfcloud.model.entity.BusinessWorkFlowAttr;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowAttrExtendVO;

import java.util.List;

/**
 * 业务流程属性配置 Mapper 接口
 * @author:dailongting
 * @date:2019-06-15 17:57
 */
public interface BusinessWorkFlowAttrMapper extends BaseMapper<BusinessWorkFlowAttr> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessWorkFlowAttr record);

    int batchInsert(List<BusinessWorkFlowAttr> list);

    int insertSelective(BusinessWorkFlowAttr record);

    BusinessWorkFlowAttr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessWorkFlowAttr record);

    int updateByPrimaryKey(BusinessWorkFlowAttr record);

    List<BusinessWorkFlowAttrExtendVO> getBusinessWorkFlowAttrExtendList();

    int batchUpdateAttr(List<AddWorkFlowAttrDTO> list);
}