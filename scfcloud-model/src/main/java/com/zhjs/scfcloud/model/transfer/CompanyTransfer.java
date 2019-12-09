package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.entity.CompanyAudit;
import com.zhjs.scfcloud.model.vo.CompanyAuditBasicVO;
import com.zhjs.scfcloud.model.vo.CompanyAuditListVO;
import com.zhjs.scfcloud.model.vo.CompanyListVO;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 公司数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 17:36
 *
 * @author liuchanghai
 * @create 2019-05-21 17:36
 * @since
 */

@Mapper(componentModel = "spring")
public interface CompanyTransfer {

    List<CompanyListVO> companys2CompanyListVo(List<Company> list);

    CompanyAudit companyRegDTO2Po(CompanyRegDTO dto);

    List<CompanyAuditListVO> companyAudits2AuditListVo(List<CompanyAudit> list);

    Company companyAuditDto2CompanyPo(CompanyAuditDTO dto);

    CompanyAuditBasicVO toCompanyAuditBasicVO(CompanyAudit companyAudit);

    List<CompanySelectVO> companyList2CompanySelectVO(List<Company> list);

    CompanyAudit companyAuditDTO2CompanyAudit(CompanyAuditDTO dto);

    Company regDTO2Po(CompanyRegDTO dto);
}
