package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.entity.CreditApplyAudit;
import com.zhjs.scfcloud.model.vo.credit.CreditApprovalCourseVO;
import com.zhjs.scfcloud.model.vo.credit.CreditAuditVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业务审批表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-24 11:44
 */
public interface CreditApplyAuditMapper extends BaseMapper<CreditApplyAudit> {
    int deleteByPrimaryKey(Long id);

    int insert(CreditApplyAudit record);

    int insertSelective(CreditApplyAudit record);

    CreditApplyAudit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreditApplyAudit record);

    int updateByPrimaryKey(CreditApplyAudit record);

    int updateByPrimaryKeyWithBLOBs(CreditApplyAudit record);

    /**
     * 查询审核记录
     * @param auditLogListDTO
     * @return
     */
    List<CreditAuditVO> findAuditLogList(AuditLogListDTO auditLogListDTO);

    /**
     * 查询字段的值
     * @param companyId
     * @param businessId
     * @param businessTypeId
     * @param projectId
     * @param tableName
     * @param columnName
     * @return
     */
    String findFieldOldValueBy(@Param("companyId") Long companyId, @Param("businessId") Long businessId,
                               @Param("businessTypeId") Long businessTypeId, @Param("projectId") Long projectId,
                               @Param("tableName") String tableName, @Param("columnName") String columnName);

    /**
     * 更新字段的值
     * @param companyId
     * @param businessId
     * @param businessTypeId
     * @param projectId
     * @param tableName
     * @param columnName
     * @param columnValue
     * @return
     */
    int updateFieldNewValueBy(@Param("companyId") Long companyId, @Param("businessId") Long businessId,
                              @Param("businessTypeId") Long businessTypeId, @Param("projectId") Long projectId,
                              @Param("tableName") String tableName, @Param("columnName") String columnName,
                              @Param("columnValue") String columnValue);

    Map<String, Object> findAuditDataObj(@Param("tableName") String tableName, @Param("companyId") Long companyId, @Param("businessId") Long businessId, @Param("businessIdColumnName") String businessIdColumnName, @Param("projectId")String projectId);

    int updateAuditData(Map<String, Object> paramMap);

    int insertAuditData(Map<String, Object> paramMap);

    /**
     * 查询审核过程（最新）
     * @param businessId
     * @return
     */
    List<CreditApprovalCourseVO> findApprovalCourse(@Param("businessId") Long businessId, @Param("companyId") Long companyId, @Param("businessTypeId") Long businessTypeId);
}