package com.zhjs.scfcloud.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.dto.FindCompanyListDTO;
import com.zhjs.scfcloud.model.vo.CompanyListVO;
import com.zhjs.scfcloud.system.service.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyServiceTest {

    @Autowired
    CompanyService companyService;

    @Test
    public void selectCompanySystemVersion() {
        FindCompanyListDTO dto = new FindCompanyListDTO();
        dto.setCurrent(1L);
        dto.setSize(1L);
        Page<CompanyListVO> companyPage = companyService.selectCompanyListPage(dto);
        System.out.println(companyPage.getRecords());
    }
}
