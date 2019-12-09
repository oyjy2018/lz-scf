package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.jr.jropen.sdk.model.CommonResponse;
import com.jd.jr.jropen.sdk.model.enter.EnterRegisterResponseBody;
import com.zhjs.scfcloud.model.entity.CompanyJdVerified;
import com.zhjs.scfcloud.model.mapper.CompanyJdVerifiedMapper;
import com.zhjs.scfcloud.system.service.CompanyJdVerifiedService;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.jd.JdEnterpriseInfoService;
import com.zhjs.scfcloud.util.util.EmailTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 京东账户服务
 */
@Service
@PropertySource("classpath:jropen_sdk.properties")
public class CompanyJdVerifiedServiceImpl extends ServiceImpl<CompanyJdVerifiedMapper, CompanyJdVerified> implements CompanyJdVerifiedService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EmailTool emailTool;
    @Value("${mail.username}")
    private String senderMailAddress;

    @Override
    @Async
    public void companyRegister(Long companyId, String mobile) {
        CommonResponse<EnterRegisterResponseBody> response = JdEnterpriseInfoService.register(companyId.toString(),mobile);
        //京东请求失败
        if(!"00000".equals(response.getResponseHeader().getCode())){
            logger.info("发起京东注册-请求失败："+response.getResponseHeader().getMsg());
            //邮件通知
            sendJdRegisterEmail(companyId,response.getResponseHeader().getMsg());
            return;
        }
        EnterRegisterResponseBody registerBody =  response.getResponseBody();
        //京东注册失败
        if(!"success".equals(registerBody.getResponseCode())){
            logger.info("发起京东注册-注册失败："+registerBody);
            //邮件通知
            sendJdRegisterEmail(companyId,registerBody.getResponseDesc());
            return;
        }
        //京东注册成功，插入数据
        CompanyJdVerified companyJdVerified = new CompanyJdVerified();
        companyJdVerified.setCompanyId(companyId);
        companyJdVerified.setJrMerchantid(registerBody.getJrMerchantId());
        companyJdVerified.setIsRegister(1);
        companyJdVerified.setMerRealStatus(0);
        save(companyJdVerified);
    }

    private void sendJdRegisterEmail(Long companyId,String reason){
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{senderMailAddress});
        emailVO.setTitle("京东注册失败通知");
        emailVO.setTemplate("JdRegisterTemplate.html");
        Map<String,Object> variables = new HashMap<>();
        variables.put("subject","京东注册失败通知");
        variables.put("companyInfo","京东注册失败的公司ID为:"+companyId);
        variables.put("reason","失败原因:"+reason);
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
    }

    @Override
    public String selectRealUrl(Long companyId){
        return JdEnterpriseInfoService.initJdLoginUrl(companyId.toString());
    }
}
