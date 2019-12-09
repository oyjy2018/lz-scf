package com.zhjs.scfcloud;

import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketOrderAllListDTO;
import com.zhjs.scfcloud.service.TicketOrderService;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketOrderTest {

    @Autowired
    TicketOrderService ticketOrderService;

    @Autowired
    private JDZPUtil jdzpUtil;

    @Test
    public void testGetAllTicketOrderList() {
        BusinessTicketOrderAllListDTO dto = new BusinessTicketOrderAllListDTO();
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(ticketOrderService.getAllOrderList(dto));
    }

    @Test
    public void queryUserCenterUrl() {
        System.out.println(JsonUtil.toSerializerJSON(jdzpUtil.queryUserCenterUrl("天综帽创特管工有限公司","62")));
    }

}
