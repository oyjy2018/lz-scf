package com.zhjs.scfcloud.system;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyAuditListQueryDTO;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.system.service.CreditApplyService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditApplyTest {

    @Resource
    CreditApplyService creditApplyService;
    @Test
    public void auditList(){
        long begin = System.currentTimeMillis();
        CreditApplyAuditListQueryDTO dto = new CreditApplyAuditListQueryDTO();
        dto.setUserId(1l);
        System.out.println(JsonUtil.toJSON(creditApplyService.auditList(dto)));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
    }

    @Test
    public void getBusinessAttrVal(){
        long begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1489l)));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1470l)));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1459l)));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
    }

    @Test
    public void getBusinessAttrVal1(){
        long begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1489l,89l,34l,1724l)));
        System.out.println("service1 spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1470l,89l,34l,1703l)));
        System.out.println("service2 spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1459l,89l,34l,1697l)));
        System.out.println("service3 spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1459l,89l,34l,null)));
        System.out.println("service4 spend:"+(System.currentTimeMillis()-begin)+"ms");
        begin = System.currentTimeMillis();
        System.out.println(JsonUtil.toJSON(BusinessCfgUtil.getBusinessAttrVal(1459l)));
        System.out.println("service5 spend:"+(System.currentTimeMillis()-begin)+"ms");
    }

    @Test
    public void allApplyList(){
        long begin = System.currentTimeMillis();
        CreditApplyAllListQueryDTO dto = new CreditApplyAllListQueryDTO();
        dto.setUserId(444l);
        dto.setRoleIds("1,2,3");
        System.out.println(creditApplyService.allApplyList(dto));
        System.out.println("service spend:"+(System.currentTimeMillis()-begin)+"ms");
    }
}
