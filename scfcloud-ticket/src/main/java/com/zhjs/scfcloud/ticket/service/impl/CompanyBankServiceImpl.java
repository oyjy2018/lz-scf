package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.dto.ticket.PermissionBankListDTO;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.model.mapper.UserMapper;
import com.zhjs.scfcloud.ticket.service.CompanyBankService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Service
public class CompanyBankServiceImpl implements CompanyBankService {
    private Logger logger = LoggerFactory.getLogger(CompanyBankServiceImpl.class);
    @Autowired
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<CompanyBankAccount> findCompanyBanksByUserPermission(Long userId, PermissionBankListDTO permissionBankListDTO) {
        User user = userMapper.selectById(userId);
        if(StringUtils.isEmpty(user.getPermissionOrgIds())){
            return new Page<>(permissionBankListDTO.getCurrent(),permissionBankListDTO.getSize());
        }
        QueryWrapper<CompanyBankAccount> bankAccountQueryWrapper = new QueryWrapper<>();
        bankAccountQueryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        bankAccountQueryWrapper.in("company_id", Arrays.asList(user.getPermissionOrgIds().split(",")));
        if(!StringUtils.isEmpty(permissionBankListDTO.getCompanyName())){
            bankAccountQueryWrapper.like("bank_account_name",permissionBankListDTO.getCompanyName());
        }
        if(!StringUtils.isEmpty(permissionBankListDTO.getAccountType())){
            bankAccountQueryWrapper.like("account_type",permissionBankListDTO.getAccountType());
        }
        if(permissionBankListDTO.getAccountStatus() != null){
            bankAccountQueryWrapper.eq("account_status", permissionBankListDTO.getAccountStatus());
        }
        bankAccountQueryWrapper.orderByDesc("update_time");
        return companyBankAccountMapper.selectPage(new Page<>(permissionBankListDTO.getCurrent(),permissionBankListDTO.getSize()),bankAccountQueryWrapper);
    }
}
