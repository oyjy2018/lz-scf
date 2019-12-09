package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.business.AddWorkFlowFileDTO;
import com.zhjs.scfcloud.model.entity.BusinessWorkFlowFile;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;

import java.util.List;

/**
 * 流程扭转附件配置 Mapper 接口
 * @author:dailongting
 * @date:2019-06-21 15:31
 */
public interface BusinessWorkFlowFileMapper extends BaseMapper<BusinessWorkFlowFile> {
    int deleteByPrimaryKey(Long id);

    int insert(BusinessWorkFlowFile record);

    int batchInsert(List<BusinessWorkFlowFile> list);

    int insertSelective(BusinessWorkFlowFile record);

    BusinessWorkFlowFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusinessWorkFlowFile record);

    int updateByPrimaryKey(BusinessWorkFlowFile record);

    /**
     * 查询所有流程扭转附件配置信息
     * @return
     */
    List<BusinessWorkFlowFileVO> findWorkFlowFileCfg();

    /**
     * 批量修改流程配置文件
     * @param list
     * @return
     */
    int batchUpdateFile(List<AddWorkFlowFileDTO> list);
}