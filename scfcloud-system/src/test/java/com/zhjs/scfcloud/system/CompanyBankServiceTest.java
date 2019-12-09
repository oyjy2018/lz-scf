package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;
import com.zhjs.scfcloud.system.service.CompanyBankAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyBankServiceTest {

    @Autowired
    CompanyBankAccountService companyBankAccountService;

    @Test
    public void contextLoads() {
        //设置默认账户
        //companyBankAccountService.setDefault(new Long(45));
        //ECDS授权
        System.out.println(companyBankAccountService.paymentBank(new Long(52)));
    }

    @Test
    public void verifyBank() {
        CompanyBankVerifyVO companyBankVerifyVO = new CompanyBankVerifyVO();
        companyBankVerifyVO.setCompanyBankId(91L);
        companyBankVerifyVO.setAmount(new BigDecimal("0.06"));
        Result result = companyBankAccountService.verifyBank(companyBankVerifyVO);
    }

    @Test
    public void setDefault() {
        companyBankAccountService.setDefault(89L,"1");
    }

}
