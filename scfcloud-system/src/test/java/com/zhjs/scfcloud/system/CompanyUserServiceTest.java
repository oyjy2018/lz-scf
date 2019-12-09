package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.dto.CompanyUserInfoEditDTO;
import com.zhjs.scfcloud.system.service.CompanyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyUserServiceTest {

    @Autowired
    CompanyUserService companyUserService;

    @Test
    public void findCompanyUserInfo() {
        System.out.println(companyUserService.findCompanyUserInfo(1L));
    }

    @Test
    public void updateCompanyUserInfo() {
        CompanyUserInfoEditDTO companyUserInfoEditDTO = new CompanyUserInfoEditDTO();
        companyUserInfoEditDTO.setUserName("修改名称测试");
        companyUserInfoEditDTO.setGender(2);
        companyUserService.updateCompanyUserInfo(69L, companyUserInfoEditDTO);
    }

    @Test
    public void checkCompanyUserPassword() {
        System.out.println(companyUserService.checkCompanyUserPassword(1L, "1234567"));
    }

    @Test
    public void updateUserEmail() {

        System.out.println(companyUserService.checkCompanyUserPassword(1L, "1234567"));
    }

    @Test
    public void updateUserPhone() {
        System.out.println(companyUserService.checkCompanyUserPassword(1L, "1234567"));
    }
}
