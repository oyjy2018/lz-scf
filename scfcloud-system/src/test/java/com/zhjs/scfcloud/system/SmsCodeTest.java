package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.system.service.SmsCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsCodeTest {
    @Autowired
    SmsCodeService smsCodeService;

    @Test
    public void sendSms(){
//        System.out.println(smsCodeService.sendSms("17676163266"));
    }


}
