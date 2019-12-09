package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.system.service.RiskControlModelService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RiskControlModelTest {

    @Autowired
    RiskControlModelService riskControlModelService;

    @Test
    public void testGetAllTicketOrderList() {
        RiskControlModelInsertDTO dto = new RiskControlModelInsertDTO();
        dto.setCompanyId(1l);
        dto.setUserId(1l);
        dto.setSystemId("1");
        dto.setBusinessTypeId("1");
        dto.setBusinessTypeName("申请授信");
        dto.setModelName("骗子戏法");
        dto.setModelExplain("不要贪图小便宜");
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(riskControlModelService.insert(dto));
    }


    @Test
    public void detail() {
        System.out.println(JsonUtil.toSerializerJSON(riskControlModelService.detail(2l)));
    }

    @Test
    public void getModelFormula() {
        System.out.println(JsonUtil.toSerializerJSON(riskControlModelService.getModelFormula(2l)));
    }

}
