package com.zhjs.scfcloud.ticket;

import com.jd.jr.jropen.sdk.model.CommonResponse;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.QuotationDTO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketQuotationService;
import com.zhjs.scfcloud.util.jd.JdEnterpriseInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScfcloudTicketApplicationTests {

    @Resource
    BusinessTicketQuotationService service;

    @Test
    public void contextLoads() {

        QuotationDTO dto = new QuotationDTO();

        Result result = service.acceptedQuotation(dto);
        System.out.println(result.toJSON());
    }



}
