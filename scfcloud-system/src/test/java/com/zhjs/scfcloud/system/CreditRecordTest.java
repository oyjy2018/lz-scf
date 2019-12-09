package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordBalanceQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordImportDTO;
import com.zhjs.scfcloud.system.service.CreditRecordService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditRecordTest {
    @Autowired
    CreditRecordService creditRecordService;

    @Test
    public void importRecord() {
        CreditRecordImportDTO dto = new CreditRecordImportDTO();
        dto.setFileUrl("C:\\Users\\Administrator\\Desktop\\导入授信记录模板-1.xlsx");
        dto.setCompanyId(1l);
        System.out.println(JsonUtil.toSerializerJSON(creditRecordService.importRecord(dto)));
    }

    @Test
    public void allList(){
        CreditRecordAllListQueryDTO dto = new CreditRecordAllListQueryDTO();
        dto.setIdCard("331125194603183669");
        System.out.println(creditRecordService.allList(dto));
    }

    @Test
    public void delete(){
        BaseDeleteDTO dto = new BaseDeleteDTO();
        dto.setId(1l);
        dto.setUserId(1l);
        dto.setUserName("渴了吧");
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(JsonUtil.toSerializerJSON(creditRecordService.deleteRecord(dto)));
    }

    @Test
    public void findCreditBalance(){
        CreditRecordBalanceQueryDTO dto = new CreditRecordBalanceQueryDTO();
        dto.setPartnerId("fdggf");
        dto.setProjectId("ss11");
        dto.setManagerName("戴总");
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(JsonUtil.toSerializerJSON(creditRecordService.findCreditBalance(dto)));
    }
}
