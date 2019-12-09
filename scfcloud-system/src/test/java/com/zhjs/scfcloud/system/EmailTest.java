package com.zhjs.scfcloud.system;

import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void testSendEmail(){
//        EmailVO emailVO = new EmailVO();
//        emailVO.setTo(new String[]{senderMailAddress});
//        emailVO.setTemplate("NoticeTemplate.html");
//        emailVO.setTitle("【领筑金融云】有新的公司注册");
//        Map<String,Object> variables = new HashMap<>();
//        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
//        variables.put("firstH","系统管理员");
//        variables.put("content","您好，有新的公司提交了注册申请，请到平台审核。");
//        variables.put("url", riskFrontEndUrl);
//        variables.put("linkContent","点此进入领筑风控平台");
//        variables.put("hasSecondUrl",false);
//        emailVO.setVariables(variables);
//        emailTool.commonSendMail(emailVO);


        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑金融云】公司注册成功提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+"公司名称+"+"】");
        variables.put("content","您好，平台已审核通过您的注册申请，您可以到平台【设置-成员管理】、【设置-权限管理】页面新增公司各个业务的操作人员。");
        variables.put("url", riskFrontEndUrl);
        variables.put("linkContent","点此进入领筑风控平台");
        variables.put("hasSecondUrl",true);
        variables.put("secondUrl", billFrontEndUrl+"/bill/login");
        variables.put("secondlinkContent", "点此进入领筑票据融资平台");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
    }
}
