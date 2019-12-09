package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.entity.CompanyAudit;
import com.zhjs.scfcloud.model.vo.CompanyAuditListVO;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 公司审核信息表 Mapper 接口
 * @author:dailongting
 * @date:2019-07-22 11:19
 */
public interface CompanyAuditMapper extends BaseMapper<CompanyAudit> {
    /**
     * 分页查询 公司审核列表数据
     * @param page
     * @param status
     * @return
     */
    List<CompanyAuditListVO> selectCompanyAuditList(Page<CompanyAuditListVO> page, @Param("status") Integer status);

    /**
     * 给公司赋予流程配置
     * @param companyId
     * @param flowIds
     */
    void initBusinessFlow(@Param("companyId") Long companyId,@Param("flowIds") String flowIds);
}