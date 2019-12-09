package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyAuditDetailDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import com.zhjs.scfcloud.model.entity.CompanyAudit;
import com.zhjs.scfcloud.model.vo.CompanyAuditListVO;

/**
 * 公司审核服务
 * @author weijie.chen
 */
public interface CompanyAuditService extends IService<CompanyAudit> {

    /**
     * 分页查询 公司审核列表数据
     * @param dto
     * @return
     */
    Page<CompanyAuditListVO> selectCompanyAuditList(FindCompanyAuditListDTO dto);

    /**
     * 查看审核信息详情
     * @param companyAuditId
     * @return
     */
    CompanyAuditDetailDTO selectCompanyAuditDetail(Long companyAuditId);

    /**
     * 公司注册
     * @param dto
     */
    Result companyRegister(CompanyRegDTO dto);

    /**
     * 公司审核
     * @param companyAuditId
     * @param auditUserId
     * @param dto
     * @return
     */
    Result companyAudit(Long companyAuditId,Long auditUserId,CompanyAuditDTO dto);

    /**
     * 公司激活
     * @param companyAuditId
     * @return
     */
    Result companyActive(Long companyAuditId);

    Result sendCompanyActiveEmail(Long companyAuditId);

    /**
     * E签宝注册
     * @param companyId
     * @param audit
     */
    public void eSginRegister(Long companyId,CompanyAudit audit);

    /**
     * 公司JD异步注册
     * @param companyId
     */
    Result companyJdAsyncRegister(Long companyId);

    /**
     * 个人账号E签宝-异步注册
     * @param companyId
     */
    Result personEsginAsyncRegister(Long companyId);

    /**
     * 企业账号E签宝-异步注册
     * @param companyId
     */
    Result companyEsignAsyncRegister(Long companyId);
}
