package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.system.service.LzProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzProjectTest {

    @Autowired
    LzProjectService lzProjectService;


    @Test
    public void getPayList() {
        System.out.println(lzProjectService.payList("ct001"));
    }

    @Test
    public  void generateData() {
        lzProjectService.generateData();
    }
}
