package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.QuotationTicketQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorAddDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorListDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorUpdateDTO;
import com.zhjs.scfcloud.model.vo.QuotationTicketVO;
import com.zhjs.scfcloud.ticket.service.AcceptorService;
import com.zhjs.scfcloud.ticket.service.QuotationService;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcceptorServiceTests {

    @Resource
    AcceptorService acceptorService;

    @Test
    public void add() {
        AcceptorAddDTO dto = new AcceptorAddDTO();
        dto.setAcceptorName("骗子公司");
        dto.setLimitMoney("1000");
        dto.setRemark("这是备注哦");
        dto.setCompanyId(1l);
        dto.setUserId(1l);
        System.out.println(acceptorService.add(dto));
    }

    @Test
    public void list() {
        AcceptorListDTO dto = new AcceptorListDTO();
        dto.setCompanyId(66L);
        //dto.setEffectiveBeginDateBegin("2019-11-01");
        System.out.println(JsonUtil.toJSON(acceptorService.findList(dto)));
    }

    @Test
    public void delete() {
        BaseDeleteDTO dto = new BaseDeleteDTO();
        dto.setId(1L);
        dto.setUserId(2l);
        //dto.setEffectiveBeginDateBegin("2019-11-01");
        System.out.println(JsonUtil.toJSON(acceptorService.delete(dto)));
    }

    @Test
    public void update() {
        AcceptorUpdateDTO dto = new AcceptorUpdateDTO();
        dto.setId(1L);
        dto.setLimitMoney("30000");
        dto.setRemark("修改了额度");
        dto.setUserId(1l);
        System.out.println(acceptorService.update(dto));
    }
}
