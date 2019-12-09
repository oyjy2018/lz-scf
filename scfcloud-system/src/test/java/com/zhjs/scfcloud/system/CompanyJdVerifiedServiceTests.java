package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.system.service.CompanyJdVerifiedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyJdVerifiedServiceTests {
    @Autowired
    CompanyJdVerifiedService companyJdVerifiedService;

    /**
     * 测试京东注册
     */
    @Test
    public void testRegister(){
        System.out.println("==========================================");
        companyJdVerifiedService.companyRegister(new Long(1),"13144800366");
        System.out.println("===========================================");
    }

    /**
     * 获取京东实名认证URL
     */
    @Test
    public void testRealUrl(){
        System.out.println("==========================================");
        System.out.println("实名认证链接：" + companyJdVerifiedService.selectRealUrl(new Long(43)));
        System.out.println("===========================================");
    }
}
