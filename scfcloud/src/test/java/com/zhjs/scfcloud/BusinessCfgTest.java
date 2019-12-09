package com.zhjs.scfcloud;

import com.zhjs.scfcloud.service.BusinessCfgService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessCfgTest {

    @Autowired
    BusinessCfgService businessCfgService;



    @Test
    public void getBusinessTypeList() {
        System.out.println(JsonUtil.toSerializerJSON(businessCfgService.getBusinessTypeList(1l)));
    }

}
