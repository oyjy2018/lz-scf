package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.entity.CreditUse;
import com.zhjs.scfcloud.system.service.CreditTicketApplyService;
import com.zhjs.scfcloud.system.service.CreditUseService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditTicketApplyTest {
    @Autowired
    CreditTicketApplyService applyService;
    @Autowired
    CreditUseService creditUseService;
    @Autowired
    CreditTicketApplyService creditTicketApplyService;

    @Test
    public void allList() {
        CreditUseApplyAllListQueryDTO dto = new CreditUseApplyAllListQueryDTO();
        dto.setUserId(1l);
        dto.setRoleIds("1");
        creditUseService.findCreditUseApplyAllList(dto);
    }

    @Test
    public void getAuditFinishList() {
        CreditUseApplyAuditFinishListQueryDTO dto = new CreditUseApplyAuditFinishListQueryDTO();
        dto.setUserId(1l);
        dto.setPermissionType(2);
        dto.setPermissionOrgIds("1,2,5,6,8");
        System.out.println(JsonUtil.toSerializerJSON(dto));
        Result result = creditTicketApplyService.getAuditFinishList(dto);
        System.out.println(JsonUtil.toJSON(result));
    }

    @Test
    public void getRemainCreditAmount() {
        Result result = creditTicketApplyService.getRemainCreditAmount(188l);
        System.out.println(JsonUtil.toJSON(result));
    }
}
