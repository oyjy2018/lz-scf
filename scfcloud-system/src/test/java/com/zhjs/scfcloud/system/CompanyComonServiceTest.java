package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.vo.CompanySystemVersionNameVO;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyComonServiceTest {

    @Autowired
    CompanyCommonService companyCommonService;

    @Test
    public void selectCompanySystemVersion() {
        CompanySystemVersionNameVO result = companyCommonService.selectCompanySystemVersion(50L);
        System.out.println(result);
    }
}
