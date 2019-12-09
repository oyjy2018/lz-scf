package com.zhjs.scfcloud;

import com.zhjs.scfcloud.feign.LzProjectFeignService;
import com.zhjs.scfcloud.model.dto.lzProject.LzProjectListDTO;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzProjectTest {

    @Autowired
    LzProjectFeignService lzProjectFeignService;

    @Test
    public void list() {
        LzProjectListDTO dto = new LzProjectListDTO();
        dto.setBusinessManager("老李");
        System.out.println(JsonUtil.toJSON(lzProjectFeignService.list(dto)));
    }

    @Test
    public void payList() {
        System.out.println(JsonUtil.toJSON(lzProjectFeignService.payList("ct001")));
    }
}
