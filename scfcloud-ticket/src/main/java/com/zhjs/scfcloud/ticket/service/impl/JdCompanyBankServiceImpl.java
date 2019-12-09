package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.model.mapper.CompanyMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.ticket.service.JdCompanyBankService;
import com.zhjs.scfcloud.util.VO.EmailVO;
import com.zhjs.scfcloud.util.enums.JdVerifiedEnum;
import com.zhjs.scfcloud.util.util.EmailTool;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class JdCompanyBankServiceImpl implements JdCompanyBankService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyBankAccountMapper companyBankAccountMapper;
    @Autowired
    CompanyMapper companyMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private EmailTool emailTool;
    @Value("${frontEndUrl.risk}")
    private String riskFrontEndUrl;
    @Autowired
    private SmsUtil smsUtil;

    @Override
    public void updateBankStatusByAgency(Long payOrderId,String subStatus) {
        QueryWrapper<CompanyBankAccount> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.eq("pay_order_id",payOrderId);
        CompanyBankAccount bankAccount = companyBankAccountMapper.selectOne(accountQueryWrapper);
        //打款成功
        if(subStatus.equals(JdVerifiedEnum.BankSubStatus.status_13.getStatus())){
            //银行卡状态更新为认证中
            bankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status1.getStatus());
            //更新打款认证状态
            bankAccount.setSubStatus(subStatus);
            //添加打款成功时间
            bankAccount.setPaySuccessTime(LocalDateTime.now());
        }else {
            //银行卡状态更新为认证失败
            bankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status3.getStatus());
            //更新打款认证状态为打款失败
            bankAccount.setSubStatus(subStatus);
        }
        bankAccount.setUpdateTime(LocalDateTime.now());
        companyBankAccountMapper.updateById(bankAccount);
        //发送通知
        if(bankAccount.getAccountStatus() == JdVerifiedEnum.AccountStatus.status1.getStatus()){
            Company company = companyMapper.selectById(bankAccount.getCompanyId());
            User user = userMapper.selectById(company.getRegUserId());
            //时间格式化
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            sendNotice(user.getEmail(),company.getCompanyName(),user.getPhone(),dtf2.format(bankAccount.getPaySuccessTime()));
        }

    }

    private void sendNotice(String email,String companyName,String phone,String payTime){
        //发送通知邮件
        EmailVO emailVO = new EmailVO();
        emailVO.setTo(new String[]{email});
        emailVO.setTemplate("NoticeTemplate.html");
        emailVO.setTitle("【领筑票据融资平台】小额打款提醒");
        Map<String,Object> variables = new HashMap<>();
        variables.put("fileServiceUrl", FileUtil.getFileServiceUrl());
        variables.put("firstH","【"+companyName+"】");
        variables.put("content","您好，平台已委托京东金融于"+payTime+"提交一笔0.5元以下的打款申请，预计到账时间为1个工作日，请于对公账户收到金额后，到平台【设置-银行账户】页面填写到账金额。");
        variables.put("url", riskFrontEndUrl+"/settings/company/banks");
        variables.put("linkContent","点此进入银行账户页面");
        emailVO.setVariables(variables);
        emailTool.commonSendMail(emailVO);
        //发送短信
        Map<String, String> params = new HashMap<>();
        params.put("paydate",payTime);
        String res = smsUtil.lzySmsBsend(phone, SmsUtil.lzy_sms_type_S506,params);
        logger.info("短信发送成功，response：{}", res);
    }

}
