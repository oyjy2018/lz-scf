package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.CreditTicketAuditData;

import java.util.List;
import java.util.Map;

/**
 * 开商票申请审批数据表 Mapper 接口
 * @author:liuchanghai
 * @date:2019-07-03 19:57
 */
public interface CreditTicketAuditDataMapper extends BaseMapper<CreditTicketAuditData> {
    int deleteByPrimaryKey(Long id);

    int insert(CreditTicketAuditData record);

    int insertSelective(CreditTicketAuditData record);

    CreditTicketAuditData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreditTicketAuditData record);

    int updateByPrimaryKeyWithBLOBs(CreditTicketAuditData record);

    int updateByPrimaryKey(CreditTicketAuditData record);

    /**
     * 查询开票申请审批数据（字段名带表名前缀）
     * @param businessId
     * @return
     */
    Map<String, Object> findAuditDataByBusinessId(Long businessId);

    /**
     * 查询授信记录的审批通过且未用信的开票申请审批数据
     * @param record
     * @return
     */
    List<CreditTicketAuditData> findUnUseAuditDataByRecordId(Long record);
}