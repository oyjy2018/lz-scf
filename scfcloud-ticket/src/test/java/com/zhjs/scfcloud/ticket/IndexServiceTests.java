package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.model.vo.TicketPlatform.IndexVO;
import com.zhjs.scfcloud.ticket.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTests {

    @Resource
    IndexService indexService;

    @Test
    public void contextLoads() {
        IndexVO indexVO = indexService.index();
        System.out.println(indexVO);
    }



}
