package com.zhjs.scfcloud.ticket;

import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest {
    @Autowired
    private EmailTool emailTool;
    @Value("${mail.username}")
    private String senderMailAddress;
    @Value("${frontEndUrl.risk}")
    private String riskFrontEndUrl;
    @Value("${frontEndUrl.bill}")
    private String billFrontEndUrl;

    /**
     * 测试-发布询价单提醒
     */
    @Test
    public void testSendEmail(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】发布询价单提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+"卖家公司名称+"+"】");
        variables.put("content","您好，询价单编号："+"XXXXXXXX"+"，状态为【"+"待报价"+"】。");
        variables.put("url", billFrontEndUrl+"/sell/inquiredetail?id="+142);
        variables.put("linkContent","点此查看询价单详情");
        variables.put("hasSecondUrl",false);
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);

    }

    /**
     * 测试-询价单的报价提醒
     */
    @Test
    public void testSendEmail2(){
        //询价方邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】询价单的报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent","您好，询价单编号："+142+"，有新的报价：");
        variables.put("billNo","");
        variables.put("accepterName","");
        variables.put("quotationCompanyName","");
        variables.put("quotationTime","");
        variables.put("billAmt","");
        variables.put("amount","");
        variables.put("firstUrl", billFrontEndUrl+"/sell/inquiredetail?id="+142);
        variables.put("firstUrlContent","点此查看询价单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{senderMailAddress});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】报价提醒");
        Map<String,Object> quotationVariables = new HashMap<>();
        quotationVariables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        quotationVariables.put("companyName","【"+"买家公司名称"+"】");
        quotationVariables.put("firstContent","您好，贵公司对【询价单编号："+"XXXXXXXX"+"】提交了报价：");
        quotationVariables.put("billNo","");
        quotationVariables.put("accepterName","");
        quotationVariables.put("inquireCompanyName","");
        quotationVariables.put("quotationCompanyName","");
        quotationVariables.put("quotationTime","");
        quotationVariables.put("billAmt",new BigDecimal("123456.45"));
        quotationVariables.put("amount",new BigDecimal("2.00"));
        quotationVariables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+127);
        quotationVariables.put("firstUrlContent","点此查看报价单详情");
        quotationEmailVO.setVariables(quotationVariables);
        emailTool.commonSendMail(quotationEmailVO);
    }

    /**
     * 测试-接受报价提醒
     */
    @Test
    public void testSendEmail3(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent","您好，贵公司接受了"+"XXXXX"+"公司的报价【报价单编号："+127+"】：");
        variables.put("billNo","");
        variables.put("accepterName","");
        variables.put("inquireCompanyName","");
        variables.put("quotationCompanyName","");
        variables.put("quotationTime","");
        variables.put("accepterTime","");
        variables.put("billAmt","");
        variables.put("amount","");
        variables.put("secondContent","系统已创建了一笔订单【订单编号："+142+"】，订单状态为【"+"待买家签署合同"+"】。");
        variables.put("firstUrl", billFrontEndUrl+"/sell/orderdetail?orderId="+142);
        variables.put("firstUrlContent","点此查看订单详情");
        variables.put("secondUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+127);
        variables.put("secondUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{senderMailAddress});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】接受报价提醒");
        variables.put("companyName","【"+"买家公司名称"+"】");
        variables.put("firstContent","您好，"+"XXXXX"+"公司接受了贵公司的报价【报价单编号："+"XXXXXXXX"+"】：");
        quotationEmailVO.setVariables(variables);
        emailTool.commonSendMail(quotationEmailVO);
        //
        EmailVO emailByOrderInquire = new EmailVO();
        emailByOrderInquire.setTo(new String[]{senderMailAddress});
        emailByOrderInquire.setTemplate("NoticeTemplate.html");
        emailByOrderInquire.setTitle("【领筑票据融资平台】创建订单提醒");
        Map<String,Object> variablesByOrder = new HashMap<>();
        variablesByOrder.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variablesByOrder.put("firstH","【"+"卖家公司"+"】");
        variablesByOrder.put("content","您好，订单编号："+142+"，状态为【"+ BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatusCH()+"】。");
        variablesByOrder.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+142);
        variablesByOrder.put("linkContent","点此查看订单详情");
        emailByOrderInquire.setVariables(variablesByOrder);
        emailTool.commonSendMail(emailByOrderInquire);
        //买家
        EmailVO emailByOrderQuotation = new EmailVO();
        emailByOrderQuotation.setTo(new String[]{senderMailAddress});
        emailByOrderQuotation.setTemplate("NoticeTemplate.html");
        emailByOrderQuotation.setTitle("【领筑票据融资平台】创建订单提醒");
        variablesByOrder.put("firstH","【"+"买家公司"+"】");
        emailByOrderQuotation.setVariables(variablesByOrder);
        emailTool.commonSendMail(emailByOrderQuotation);
    }

    /**
     * 测试-票方（卖方）退回报价
     */
    @Test
    public void testSendEmail4(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】退回报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent"," 您好，贵公司退回了"+"XXXXX"+"公司的报价【报价单编号："+"XXXXXXXX"+"】：");
        variables.put("billNo","");
        variables.put("accepterName","");
        variables.put("inquireCompanyName","");
        variables.put("quotationCompanyName","");
        variables.put("quotationTime","");
        variables.put("rejectTime","");
        variables.put("billAmt","");
        variables.put("amount","");
        variables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+127);
        variables.put("firstUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{senderMailAddress});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】退回报价提醒");
        variables.put("companyName","【"+"买家公司名称"+"】");
        variables.put("firstContent","您好，"+"XXXXX"+"公司退回了贵公司的报价【报价单编号："+"XXXXXXXX"+"】：");
        quotationEmailVO.setVariables(variables);
        emailTool.commonSendMail(quotationEmailVO);
    }

    /**
     * 测试-资方（买方）撤回报价
     */
    @Test
    public void testSendEmail5(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】撤回报价提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent","您好，"+"XXXXX"+"公司撤回了对贵公司的报价【报价单编号："+"XXXXXXXX"+"】：");
        variables.put("billNo","");
        variables.put("accepterName","");
        variables.put("inquireCompanyName","");
        variables.put("quotationCompanyName","");
        variables.put("quotationTime","");
        variables.put("revokeTime","");
        variables.put("billAmt",new BigDecimal("123456.25"));
        variables.put("amount",new BigDecimal("654321.34"));
        variables.put("firstUrl", billFrontEndUrl+"/sell/quotationdetail?quotationId="+127);
        variables.put("firstUrlContent","点此查看报价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //报价方邮件
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{senderMailAddress});
        quotationEmailVO.setTemplate("TicketTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】撤回报价提醒");
        variables.put("companyName","【"+"买家公司名称"+"】");
        variables.put("firstContent","您好，贵公司撤回了对"+"XXXXX"+"公司的报价【报价单编号："+"XXXXXXXX"+"】：");
        quotationEmailVO.setVariables(variables);
        emailTool.commonSendMail(quotationEmailVO);
    }

    /**
     * 测试-询价已截止
     */
    @Test
    public void testSendEmail6(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("TicketTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】询价已截止提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("companyName","【"+"卖家公司名称"+"】");
        variables.put("firstContent","您好，贵公司的询价单已截止报价【询价单编号："+"XXXXXXXX"+"】：");
        variables.put("billNo","");
        variables.put("accepterName","");
        variables.put("inquireCompanyName","");
        variables.put("deadline","");
        variables.put("billAmt",22222222);
        variables.put("secondContent","如需继续发布，请登录平台操作。");
        variables.put("firstUrl", billFrontEndUrl+"/sell/inquiredetail?id="+178);
        variables.put("firstUrlContent","点此查看询价详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
    }

    /**
     * 小额打款成功-测试
     */
    @Test
    public void testSendEmail7(){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】小额打款提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+"公司名称+"+"】");
        variables.put("content","您好，平台已委托京东金融于2019年4月18日提交一笔0.5元以下的打款申请，预计到账时间为1个工作日，请于对公账户收到金额后，到平台【设置-银行账户】页面填写到账金额。");
        variables.put("url", riskFrontEndUrl+"/settings/company/banks");
        variables.put("linkContent","点此进入银行账户页面");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
    }

    /**
     * 测试-领筑系统创建订单
     */
    @Test
    public void testSendEmail8(){
        //卖家
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】创建订单提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+"卖家公司名称+"+"】");
        variables.put("content","您好，订单编号："+"XXXXXXXX"+"，状态为【"+"待买家签合同"+"】。");
        variables.put("url", billFrontEndUrl+"/sell/orderdetail?orderId="+142);
        variables.put("linkContent","点此查看订单详情");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //买家
        EmailVO quotationEmailVO = new EmailVO();
        quotationEmailVO.setTo(new String[]{senderMailAddress});
        quotationEmailVO.setTemplate("NoticeTemplate.html");
        quotationEmailVO.setTitle("【领筑票据融资平台】创建订单提醒");
        variables.put("firstH","【"+"买家公司名称+"+"】");
        quotationEmailVO.setVariables(variables);
        emailTool.commonSendMail(quotationEmailVO);
    }
}
