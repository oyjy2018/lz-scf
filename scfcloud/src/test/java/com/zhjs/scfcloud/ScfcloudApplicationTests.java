package com.zhjs.scfcloud;

import com.zhjs.scfcloud.util.util.JDZPUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScfcloudApplicationTests {

    @Autowired
    JDZPUtil jdzpUtil;

    @Test
    public void contextLoads() {
        System.out.println(JsonUtil.toJSON(jdzpUtil.queryBankList().getData()));
    }

}
