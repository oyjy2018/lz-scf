package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyAuditListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyMyListQueryDTO;
import com.zhjs.scfcloud.model.entity.CreditApply;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyAllListWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyAuditListWebVO;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyMyListAppVO;
import com.zhjs.scfcloud.model.vo.credit.CreditApplyMyListWebVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

/**
 * 授信申请表 Mapper 接口
 * @author:dailongting
 * @date:2019-06-19 19:52
 */
public interface CreditApplyMapper extends BaseMapper<CreditApply> {
    int deleteByPrimaryKey(Long id);

    int insert(CreditApply record);

    int insertSelective(CreditApply record);

    CreditApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CreditApply record);

    int updateByPrimaryKey(CreditApply record);

    List<CreditApplyMyListWebVO> selectMyApplyWebList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    List<CreditApplyMyListAppVO> selectMyApplyAppList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    List<CreditApplyAllListWebVO> selectAllApplyList(CreditApplyAllListQueryDTO creditApplyAllListQueryDTO);

    List<CreditApplyAuditListWebVO> selectAuditList(CreditApplyAuditListQueryDTO creditApplyMyListQueryDTO);
    /**
     * 根据配置字段查询授信业务详情
     * @param creditApplyId
     * @return
     */
    Map<String, Object> findCreditApplyDetails(@Param("creditApplyId")Long creditApplyId);

    /**
     * 根据表明查询授信业务相关的审批数据对象
     * @param tableName
     * @param companyId
     * @param creditApplyId
     * @return
     */
    Map<String, Object> findCreditAuditDataObj(@Param("tableName")String tableName,
                                               @Param("companyId")Long companyId,
                                               @Param("creditApplyId")Long creditApplyId);

    /**
     * 根据表名与字段更新授信业务相关的审批数据表
     * @param tcList
     * @param companyId
     * @param creditApplyId
     * @return
     */
    int updateCreditAuditData(@Param("tcList")List<Map<String, Object>> tcList,
                              @Param("companyId")Long companyId,
                              @Param("creditApplyId")Long creditApplyId);

}