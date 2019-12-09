package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.vo.BankDefaultVO;
import com.zhjs.scfcloud.model.vo.CompanySystemVersionNameVO;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyStatusEnum;
import com.zhjs.scfcloud.util.enums.CompanyUserEnum;
import com.zhjs.scfcloud.util.enums.JdVerifiedEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyCommonServiceImpl implements CompanyCommonService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CompanyUserMapper companyUserMapper;
    @Autowired
    private CompanyJdVerifiedMapper companyJdVerifiedMapper;
    @Autowired
    private ESignAccountMapper eSignAccountMapper;
    @Autowired
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Autowired
    private CompanySystemVersionMapper companySystemVersionMapper;
    @Autowired
    private SystemVersionMapper systemVersionMapper;
    @Autowired
    private SystemMapper systemMapper;
    @Override
    public Integer userCountByCompanyId(Long companyId,Integer status) {
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("company_id",companyId);
        companyUserQueryWrapper.eq("is_delete", 0);
        if(status != null){
            companyUserQueryWrapper.eq("status", status);
        }
        return companyUserMapper.selectCount(companyUserQueryWrapper);
    }

    @Override
    public Integer isJdRegister(Long companyId) {
        QueryWrapper<CompanyJdVerified> jdVerifiedQueryWrapper = new QueryWrapper<>();
        jdVerifiedQueryWrapper.eq("company_id",companyId);
        jdVerifiedQueryWrapper.eq("is_register", CommonEnum.WhetherEnum.YES.getStatus());
        return companyJdVerifiedMapper.selectCount(jdVerifiedQueryWrapper) == 0 ? 0 : 1;
    }

    @Override
    public Integer isJdVerified(Long companyId) {
        QueryWrapper<CompanyJdVerified> jdVerifiedQueryWrapper = new QueryWrapper<>();
        jdVerifiedQueryWrapper.eq("company_id",companyId);
        jdVerifiedQueryWrapper.eq("mer_real_status", JdVerifiedEnum.RealStatus.status2.getStatus());
        return companyJdVerifiedMapper.selectCount(jdVerifiedQueryWrapper) == 0 ? 0 : 1;
    }

    @Override
    public BankDefaultVO isDefaultBankAccount(Long companyId) {
        BankDefaultVO result = new BankDefaultVO();
        QueryWrapper<CompanyBankAccount> bankAccountQueryWrapper = new QueryWrapper<>();
        bankAccountQueryWrapper.eq("company_id",companyId);
        bankAccountQueryWrapper.eq("account_status",JdVerifiedEnum.AccountStatus.status2.getStatus());
        List<CompanyBankAccount> bankAccountList  = companyBankAccountMapper.selectList(bankAccountQueryWrapper);
        if(bankAccountList.isEmpty()){
            result.setIsReceiptAccount(0);
            result.setIsRepayAccount(0);
            return result;
        }
        result.setIsReceiptAccount(bankAccountList.stream().filter(companyBankAccount -> companyBankAccount.getIsReceiptDefault() == 1).findFirst().isPresent() ? 1 : 0);
        result.setIsRepayAccount(bankAccountList.stream().filter(companyBankAccount -> companyBankAccount.getIsRepayDefault() == 1).findFirst().isPresent()? 1 : 0);
        return result;
    }

    @Override
    public Integer esignVerified(Long companyId) {
        QueryWrapper<ESignAccount> eSignQueryWrapper = new QueryWrapper<>();
        eSignQueryWrapper.eq("scf_account_type",0);
        eSignQueryWrapper.eq("scf_account_id",companyId);
        eSignQueryWrapper.eq("is_del",0);
        return eSignAccountMapper.selectCount(eSignQueryWrapper) == 0 ? 0 : 1;
    }

    @Override
    public CompanySystemVersionNameVO selectCompanySystemVersion(Long companyId) {
        CompanySystemVersionNameVO result = new CompanySystemVersionNameVO();
        QueryWrapper<CompanySystemVersion> companySystemVersionQueryWrapper = new QueryWrapper<>();
        companySystemVersionQueryWrapper.eq("company_id",companyId);
        List<CompanySystemVersion> companySystemVersions = companySystemVersionMapper.selectList(companySystemVersionQueryWrapper);
        if(companySystemVersions.isEmpty()) return result;

        List<Long> ids = companySystemVersions.stream().map(companySystemVersion -> companySystemVersion.getSystemVersionId()).collect(Collectors.toList());

        return this.selectCompanySystemVersion(ids);
    }

    @Override
    public CompanySystemVersionNameVO selectCompanySystemVersion(List<Long> systemVersionIds) {
        CompanySystemVersionNameVO result = new CompanySystemVersionNameVO();
        if(systemVersionIds.isEmpty()) return result;
        List<SystemVersion> versionList = systemVersionMapper.selectBatchIds(systemVersionIds);
        versionList.forEach(systemVersion -> {
            if(systemVersion.getId() == 1){
                result.setRiskSystemVersionName(systemVersion.getVersionName());
                result.setRiskSystemName(systemMapper.selectById(systemVersion.getSystemId()).getSystemName());
            }
            if(systemVersion.getId() == 2){
                result.setTicketSystemVersionName(systemVersion.getVersionName());
                result.setTicketSystemName(systemMapper.selectById(systemVersion.getSystemId()).getSystemName());
            }
        });
        return result;
    }

    @Override
    public String selectCompanySystemVersionSingleton(Long systemVersionId) {
        SystemVersion systemVersion = systemVersionMapper.selectById(systemVersionId);
        return systemVersion == null ? null : systemVersion.getVersionName();
    }

    @Override
    public List<Company> selectCompanyListByUserId(Long userId) {
        QueryWrapper<CompanyUser> companyUserQueryWrapper = new QueryWrapper<>();
        companyUserQueryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        companyUserQueryWrapper.eq("status", CompanyUserEnum.status_1.getValue());
        companyUserQueryWrapper.eq("user_id",userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectList(companyUserQueryWrapper);
        if(companyUserList.isEmpty()) return null;

        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.eq("is_delete",CommonEnum.WhetherEnum.NO.getStatus());
        companyQueryWrapper.eq("status", CompanyStatusEnum.status1.getValue());
        companyQueryWrapper.in("id",companyUserList.stream().map(companyUser -> companyUser.getCompanyId()).collect(Collectors.toList()));
        return companyMapper.selectList(companyQueryWrapper);
    }

}
