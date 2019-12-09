package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.jr.cbp.sdk.entity.billpay.response.UserAccountInfoData;
import com.jd.jr.jropen.sdk.model.enter.EnterRealNameRespBody;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.entity.CompanyJdVerified;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.model.mapper.CompanyJdVerifiedMapper;
import com.zhjs.scfcloud.ticket.service.CompanyVerifiedService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyBankAccountTypeEnum;
import com.zhjs.scfcloud.util.enums.JdVerifiedEnum;
import com.zhjs.scfcloud.util.jd.JdEnterpriseInfoService;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyVerifiedServiceImpl implements CompanyVerifiedService {
    @Autowired
    private CompanyJdVerifiedMapper companyJdVerifiedMapper;
    @Autowired
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Autowired
    private JDZPUtil jdzpUtil;

    @Override
    public void saveInfoForJdReview(String merCustomerId,String merchantId,String partnerApplyId,String auditStatus,String subAuditStatus) {
        QueryWrapper<CompanyJdVerified> companyJdVerifiedQueryWrapper = new QueryWrapper<>();
        companyJdVerifiedQueryWrapper.eq("company_id",new Long(merCustomerId));
        CompanyJdVerified companyJdVerified = companyJdVerifiedMapper.selectOne(companyJdVerifiedQueryWrapper);
        if(companyJdVerified == null) return;
        companyJdVerified.setCompanyId(new Long(merCustomerId));
        companyJdVerified.setJrMerchantid(merchantId);
        companyJdVerified.setPartnerApplyId(partnerApplyId);
        companyJdVerified.setAuditStatus(Integer.parseInt(auditStatus));
        if(!StringUtils.isEmpty(subAuditStatus)){
            companyJdVerified.setSubAuditStatus(Integer.parseInt(subAuditStatus));
        }

        companyJdVerifiedMapper.updateById(companyJdVerified);
    }

    @Override
    public void saveRealNameResult(String merCustomerId) {
        QueryWrapper<CompanyJdVerified> companyJdVerifiedQueryWrapper = new QueryWrapper<>();
        companyJdVerifiedQueryWrapper.eq("company_id",new Long(merCustomerId));
        CompanyJdVerified companyJdVerified = companyJdVerifiedMapper.selectOne(companyJdVerifiedQueryWrapper);

        EnterRealNameRespBody body = JdEnterpriseInfoService.queryEnterRealNameInfo(merCustomerId,companyJdVerified.getPartnerApplyId()).getResponseBody();

        BeanUtils.copyProperties(body.getEnterRealNameItemList().get(0),companyJdVerified);
        companyJdVerified.setMerRealStatus(JdVerifiedEnum.RealStatus.status2.getStatus());
        companyJdVerifiedMapper.updateById(companyJdVerified);


        //同步 实名注册对公账户 到 - 默认打款账户
        List<UserAccountInfoData> accountList = jdzpUtil.queryUserBindAccounts(merCustomerId, 1).getData().getAccountList();
        if(!accountList.isEmpty()){
            UserAccountInfoData infoData = accountList.get(0);
            CompanyBankAccount companyBankAccount = new CompanyBankAccount();
            companyBankAccount.setCompanyId(Long.parseLong(merCustomerId));
            companyBankAccount.setBankAccountName(companyJdVerified.getBlicCompanyName());
            companyBankAccount.setBankCode(infoData.getBankCode());
            companyBankAccount.setBankName(infoData.getBankName());
            companyBankAccount.setBankCnaps(infoData.getBankCnaps());
            companyBankAccount.setBankAccountNo(infoData.getAccountNo());
            companyBankAccount.setBindId(infoData.getBindId());

            companyBankAccount.setAccountType(CompanyBankAccountTypeEnum.type_2.getValue());
            companyBankAccount.setIsReceiptDefault(CommonEnum.WhetherEnum.NO.getStatus());
            companyBankAccount.setIsRepayDefault(CommonEnum.WhetherEnum.YES.getStatus());
            //ECDS授权
            companyBankAccount.setEcdsType(JdVerifiedEnum.EcdsType.type_0.getStatus());

            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status2.getStatus());

            companyBankAccount.setCreateTime(LocalDateTime.now());
            companyBankAccount.setUpdateTime(LocalDateTime.now());
            companyBankAccountMapper.insert(companyBankAccount);
        }
    }
}
