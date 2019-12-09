package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseCompanyPageDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.*;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.BusinessTicketInquireService;
import com.zhjs.scfcloud.ticket.service.SignService;
import com.zhjs.scfcloud.ticket.service.UserService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 商票接口测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BizTktTests {
    @Resource
    private BusinessTicketInquireService businessTicketInquireService;
    @Resource
    private SignService signService;
    @Resource
    private UserService userService;

    @Test
    public void testInquire(){
        BusinessTicketInquireAddDTO dto = new BusinessTicketInquireAddDTO();
        dto.setCompanyName("狗子公司");
        dto.setCompanyId(110l);
        dto.setPublishPerson("狗子");
        dto.setPublishPersonId(456l);
        dto.setAccepterAccount("aa1");
        dto.setAccepterBank("ac1");
        dto.setBillNo("230958400510220190529405927624");
        List<BusinessTicketFile> fileList = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            BusinessTicketFile file = new BusinessTicketFile();
            file.setFileUrl("url").setFileType("").setSort(i);
            fileList.add(file);
        }
        dto.setFileList(fileList);
        System.out.println(JsonUtil.toSerializerJSON(dto));
        System.out.println(businessTicketInquireService.inquire(dto));
    }

    @Test
    public void testInquireList(){
        BusinessTicketInquireListDTO businessTicketInquireListDTO = new BusinessTicketInquireListDTO();
//        baseCompanyPageDTO.setCompanyId(456l);
        businessTicketInquireListDTO.setMultiStatus("('"+ BusinessTicketEnum.InquireStatus.inquire_status_1.getStatus()+"','"+BusinessTicketEnum.InquireStatus.inquire_status_2.getStatus()+"')");
        Result s = businessTicketInquireService.inquireList(businessTicketInquireListDTO);
        System.out.println(JsonUtil.toSerializerJSON(s));
    }

    @Test
    public void testCancelInquire(){
        BusinessTicketInquireCancelDTO businessTicketInquireCancelDTO = new BusinessTicketInquireCancelDTO();
        businessTicketInquireCancelDTO.setUserId(421l);
        businessTicketInquireCancelDTO.setId(6l);
        Result s = businessTicketInquireService.cancelInquire(businessTicketInquireCancelDTO);
        System.out.println(JsonUtil.toSerializerJSON(s.getData()));
    }

    @Test
    public void testInquireDetail(){
        Result s = businessTicketInquireService.inquireDetail(6l);
        System.out.println(JsonUtil.toSerializerJSON(s.getData()));
    }

    @Test
    public void testGenerateContractNo(){
       // System.out.println(signService.generateContractNo());
    }

    @Test
    public void quotationCompanyList(){
        Result result =userService.quotationCompanyList(61l);
        System.out.println(JsonUtil.toJSON(result));
    }

    @Test
    public void myAssignInquireList(){

        BusinessTicketInquireMyAssignListDTO dto = new BusinessTicketInquireMyAssignListDTO();
        dto.setCompanyId(62l);
        dto.setSize(15l);

        Result result = businessTicketInquireService.myAssignInquireList(dto);
        System.out.println(JsonUtil.toJSON(result));
    }
    @Test
    public void allInquire(){
        BusinessTicketAllInquireDTO dto = new BusinessTicketAllInquireDTO();
        dto.setUserId(48l);
        dto.setPermissionType(2);
        dto.setPermissionOrgIds("66");
        dto.setStatus("1");
        System.out.println(businessTicketInquireService.allInquire(dto));
    }
}
