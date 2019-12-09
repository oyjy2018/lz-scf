package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.model.dto.ticket.PermissionBankListDTO;
import com.zhjs.scfcloud.ticket.service.CompanyBankService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyBankServiceTests {

    @Resource
    CompanyBankService companyBankService;

    @Test
    public void contextLoads() {
        PermissionBankListDTO dto = new PermissionBankListDTO();
        dto.setCurrent(1L);
        dto.setSize(5L);
        System.out.println(companyBankService.findCompanyBanksByUserPermission(1L,dto));
    }

}
