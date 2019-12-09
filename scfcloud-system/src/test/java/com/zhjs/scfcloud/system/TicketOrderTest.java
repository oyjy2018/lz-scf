package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignOrganizeCreateDTO;
import com.zhjs.scfcloud.model.dto.esign.EsignPersonCreateDTO;
import com.zhjs.scfcloud.model.entity.CompanyAudit;
import com.zhjs.scfcloud.model.mapper.CompanyAuditMapper;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.system.service.BusinessService;
import com.zhjs.scfcloud.system.service.CompanyAuditService;
import com.zhjs.scfcloud.system.service.SignService;
import com.zhjs.scfcloud.system.service.TicketOrderService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketOrderTest {

    @Autowired
    TicketOrderService ticketOrderService;
    @Resource
    protected CompanyAuditMapper companyAuditMapper;
    @Resource
    protected SignService signService;
    @Resource
    private BusinessService businessService;
    @Test
    public void initWorkFlowPermit(){
        businessService.initCompanyFlowPermit(117l);
    }

    @Test
    public void testGetAllTicketOrderList() {
        BusinessTicketOrderAllListDTO dto = new BusinessTicketOrderAllListDTO();
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(ticketOrderService.getAllOrderList(dto));
    }

    @Test
    public void  appendFlow() {
        companyAuditMapper.initBusinessFlow(61L,"1,2");
//        companyAuditMapper.initBusinessFlow(66l,"2");
    }

    @Test
    public void appendEsignAccount(){
      CompanyAudit audit = companyAuditMapper.selectById(38);
        EsignPersonCreateDTO personCreateDTO = new EsignPersonCreateDTO();
        personCreateDTO.setUserId(audit.getUserId());
        personCreateDTO.setIdNo(audit.getPorxyPersonCertNo());
        personCreateDTO.setMobile(audit.getPorxyPersonPhone());
        personCreateDTO.setName(audit.getPorxyPersonName());
        signService.createPerson(personCreateDTO);
        // 企业账号
        EsignOrganizeCreateDTO esignOrganizeCreateDTO = new EsignOrganizeCreateDTO();
        esignOrganizeCreateDTO.setCompanyId(65l);
        esignOrganizeCreateDTO.setName(audit.getCompanyName());
        esignOrganizeCreateDTO.setRegType("MERGE");
        esignOrganizeCreateDTO.setOrganCode(audit.getCreditCode());
        esignOrganizeCreateDTO.setUserType("2");
        esignOrganizeCreateDTO.setLegalIdNo(audit.getLegalPersonCertNo());
        esignOrganizeCreateDTO.setLegalName(audit.getLegalPersonName());
        esignOrganizeCreateDTO.setAddress(audit.getDetailAddr());
        signService.createOrganize(esignOrganizeCreateDTO);
    }

    @Test
    public void getRedisAttrVal(){
        long begin = System.currentTimeMillis();
        System.out.println(BusinessCfgUtil.getBusinessAttrVal(1015l));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
    }
}
