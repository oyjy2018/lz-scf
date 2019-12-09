package com.zhjs.scfcloud.ticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.Company;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.entity.CompanyJdVerified;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.mapper.*;
import com.zhjs.scfcloud.model.transfer.UserTransfer;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import com.zhjs.scfcloud.ticket.service.UserService;
import com.zhjs.scfcloud.util.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户账号信息管理业务逻辑实现类
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 14:01
 * @since
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserTransfer userTransfer;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private SystemFunctionMapper functionMapper;
    @Resource
    private CompanyUserMapper companyUserMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private CompanyJdVerifiedMapper companyJdVerifiedMapper;
    @Resource
    private CompanyBankAccountMapper companyBankAccountMapper;

    //获取公司京东实名属性
    @Override
    public Result getCompanyJdProperty(Long companyId) {
        Map retMap = new HashMap();
        //查询京东实名
        QueryWrapper<CompanyJdVerified> companyJdVerifiedQueryWrapper = new QueryWrapper<>();
        companyJdVerifiedQueryWrapper.eq("company_id",companyId);
        companyJdVerifiedQueryWrapper.eq("mer_real_status",4);
        CompanyJdVerified companyJdVerified = companyJdVerifiedMapper.selectOne(companyJdVerifiedQueryWrapper);
        retMap.put("hasReal",false);
        if (companyJdVerified != null){
            retMap.put("hasReal",true);
        }
        //查询是否绑定京东收款账户
        QueryWrapper<CompanyBankAccount> moneyBankAccountQueryWrapper = new QueryWrapper<>();
        moneyBankAccountQueryWrapper.eq("company_id",companyId);
        moneyBankAccountQueryWrapper.eq("account_status",2);
        moneyBankAccountQueryWrapper.eq("is_repay_default",1);
        List<CompanyBankAccount> moneyBankAccountList = companyBankAccountMapper.selectList(moneyBankAccountQueryWrapper);
        retMap.put("hasRepay",false);
        if (!ListUtil.isEmpty(moneyBankAccountList)){
            retMap.put("hasRepay",true);
        }
        //查询是否绑定京东收票账户
        QueryWrapper<CompanyBankAccount> ticketBankAccountQueryWrapper = new QueryWrapper<>();
        ticketBankAccountQueryWrapper.eq("company_id",companyId);
        ticketBankAccountQueryWrapper.eq("account_status",2);
        ticketBankAccountQueryWrapper.eq("is_receipt_default",1);
        List<CompanyBankAccount> ticketBankAccountList = companyBankAccountMapper.selectList(moneyBankAccountQueryWrapper);
        retMap.put("hasReceipt",false);
        if (!ListUtil.isEmpty(ticketBankAccountList)){
            retMap.put("hasReceipt",true);
        }

        return Result.success(retMap);
    }

    /**
     * 获取报价公司列表，公司类型∈（保理公司，工程公司，银行）
     * @return
     * @param companyId
     */
    @Override
    public Result quotationCompanyList(Long companyId) {
        List<CompanySelectVO> companyList = companyMapper.getQuotationCompanyList(companyId);//selectList(wrapper);
        if (ListUtil.isEmpty(companyList)) return Result.success();
        return Result.successList(companyList);
    }

}
