package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.util.util.SmsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneSendTests {

    @Autowired
    private SmsUtil smsUtil;

    @Test
    public void testPhone() {
//        System.out.println(smsUtil.lzySmsBsend("17676163266", SmsUtil.lzy_sms_type_S506,new HashMap<>()));
        //时间格式化
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, String> params = new HashMap<>();
        params.put("paydate",dtf2.format(LocalDateTime.now()));
        System.out.println(smsUtil.lzySmsBsend("17676163266", SmsUtil.lzy_sms_type_S506,params));
    }



}
