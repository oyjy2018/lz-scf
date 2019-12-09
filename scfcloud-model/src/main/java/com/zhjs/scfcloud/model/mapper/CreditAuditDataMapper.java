package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.CreditAuditData;
import com.zhjs.scfcloud.model.entity.CreditAuditDataWithBLOBs;

import java.util.Map;

/**
 * 授信申请审批数据表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-22 17:40
 */
public interface CreditAuditDataMapper extends BaseMapper<CreditAuditData> {
    int deleteByPrimaryKey(Long id);

    int insert(CreditAuditDataWithBLOBs record);

    int insertSelective(CreditAuditDataWithBLOBs record);

    CreditAuditDataWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreditAuditDataWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CreditAuditDataWithBLOBs record);

    int updateByPrimaryKey(CreditAuditData record);

    /**
     * 根据授信申请ID查询授信审批数据（返回字段名待表前缀）
     * @param creditApplyId
     * @return
     */
    Map<String, Object> findAuditDataById(Long creditApplyId);
}