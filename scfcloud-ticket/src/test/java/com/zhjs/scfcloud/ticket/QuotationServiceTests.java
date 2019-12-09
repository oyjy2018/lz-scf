package com.zhjs.scfcloud.ticket;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhjs.scfcloud.model.dto.QuotationTicketQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.AllQuotationListDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import com.zhjs.scfcloud.model.entity.BusinessTicketOrder;
import com.zhjs.scfcloud.model.entity.BusinessTicketQuotation;
import com.zhjs.scfcloud.model.mapper.BusinessTicketInquireMapper;
import com.zhjs.scfcloud.model.mapper.BusinessTicketOrderMapper;
import com.zhjs.scfcloud.model.mapper.BusinessTicketQuotationMapper;
import com.zhjs.scfcloud.model.vo.businessTicket.AllQuotationTicketVO;
import com.zhjs.scfcloud.ticket.service.QuotationService;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuotationServiceTests {

    @Resource
    QuotationService quotationService;
    @Autowired
    BusinessTicketQuotationMapper quotationMapper;
    @Autowired
    BusinessTicketInquireMapper inquireMapper;
    @Autowired
    BusinessTicketOrderMapper orderMapper;
    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;


    @Test
    public void selectQuotationTicketList() {
        QuotationTicketQueryDTO dto = new QuotationTicketQueryDTO();
        dto.setInquireCompanyId(new Long(1));
        System.out.println(quotationService.selectQuotationTicketList(dto));
    }

    @Test
    public void selectAllQuotations() {
        AllQuotationListDTO dto = new AllQuotationListDTO();
        dto.setCurrent(1L);
        dto.setSize(5L);
        IPage<AllQuotationTicketVO> page =  quotationService.findAllQuotations(dto,1L);
        System.out.println(page);
    }

//    @Test
//    public void selectQuotationDetail() {
//        QuotationDetailVO result =  quotationService.selectQuotationDetail(new Long(1));
//        System.out.println(result);
//    }


//    @Test
//    public void selectQuotationOwnerList() {
//        QuotationOwnerQueryDTO dto = new QuotationOwnerQueryDTO();
//        dto.setQuotationCompanyId(new Long(43));
//        dto.setQuotationStatus(2);
//        dto.setInquireCompanyName("技术服务有限");
//        List<QuotationOwnerVO> result = quotationService.selectQuotationOwnerList(dto);
//        System.out.println(result);
//    }
    @Test
    public void testEmail() {
        BusinessTicketOrder order = orderMapper.selectById(14);
        sendAcceptedQuotationEmail(quotationMapper.selectById(order.getQuotationId()),inquireMapper.selectById(order.getInquireId()),order.getId());
    }

    private void sendAcceptedQuotationEmail(BusinessTicketQuotation quotation,BusinessTicketInquire inquire,Long orderId){
        String inquireEmail = "chenweijie@zhihuikg.com";
        String quotationEmail = "173293955@qq.com";
        //询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{inquireEmail});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+inquire.getCompanyName()+"】");
        variables.put("firstContent","您好，贵公司接受了"+quotation.getCompanyName()+"的报价【报价单编号："+quotation.getId()+"】：");
        variables.put("billNo",inquire.getBillNo());
        variables.put("accepterName",inquire.getAccepterName());
        variables.put("inquireCompanyName",inquire.getCompanyName());
        variables.put("quotationCompanyName",quotation.getCompanyName());
        variables.put("quotationTime",DateUtil.format(quotation.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("accepterTime",DateUtil.format(quotation.getAcceptedTime(),"yyyy-MM-dd HH:mm:ss"));
        variables.put("billAmt",inquire.getBillAmt());
        variables.put("amount",quotation.getAmount());
        variables.put("secondContent","系统已创建了一笔订单【订单编号："+orderId+"】，订单状态为【"+ BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】。");
        variables.put("firstUrl", billFrontEndUrl+"/sell/orderdetail?orderId="+orderId);
        variables.put("firstUrlContent","点此查看订单详情");
        variables.put("secondUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+quotation.getId());
        variables.put("secondUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);

        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{quotationEmail});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        Map<String,Object> mapCopy = variables.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapCopy.put("companyName","【"+quotation.getCompanyName()+"】");
        mapCopy.put("firstContent","您好，"+inquire.getCompanyName()+"接受了贵公司的报价【报价单编号："+quotation.getId()+"】：");
        quotationEmailVO.setVariables(mapCopy);
        emailTool.commonSendMail(quotationEmailVO);

        //订单邮件
        //卖家
//        EmailVO emailByOrderInquire = new EmailVO();
//        emailByOrderInquire.setTo(new String[]{inquireEmail});
//        emailByOrderInquire.setTemplate("NoticeTemplate.html");
//        emailByOrderInquire.setTitle("【领筑票据融资平台】创建订单提醒");
//        Map<String,Object> variablesByOrder = new HashMap<>();
//        variablesByOrder.put("fileServiceUrl", FileUtil.getFileServiceUrl());
//        variablesByOrder.put("firstH","【"+inquire.getCompanyName()+"】");
//        variablesByOrder.put("content","您好，订单编号："+orderId+"，状态为【"+BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】。");
//        variablesByOrder.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+orderId);
//        variablesByOrder.put("linkContent","点此查看订单详情");
//        emailByOrderInquire.setVariables(variablesByOrder);
//        emailTool.commonSendMail(emailByOrderInquire);
//        //买家
//        EmailVO emailByOrderQuotation = new EmailVO();
//        emailByOrderQuotation.setTo(new String[]{quotationEmail});
//        emailByOrderQuotation.setTemplate("NoticeTemplate.html");
//        emailByOrderQuotation.setTitle("【领筑票据融资平台】创建订单提醒");
//        Map<String,Object> mapCopyQuotation = variablesByOrder.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
//        mapCopyQuotation.put("firstH","【"+quotation.getCompanyName()+"】");
//        emailByOrderQuotation.setVariables(variablesByOrder);
//        emailTool.commonSendMail(emailByOrderQuotation);
//        //发送短信
//        Map<String, String> params = new HashMap<>();
//        params.put("orderNo",orderId.toString());
//        params.put("orderStatus",BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH());
//        smsUtil.lzySmsBsend(quotationUser.getPhone(), SmsUtil.lzy_sms_type_S501,params);
    }
}
