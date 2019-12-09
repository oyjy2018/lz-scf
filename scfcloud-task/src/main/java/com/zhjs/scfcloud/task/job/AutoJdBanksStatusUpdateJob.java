package com.zhjs.scfcloud.task.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.jr.cbp.sdk.entity.billpay.response.UserAccountInfoData;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.JdVerifiedEnum;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 银行卡认证状态修改-定时器
 * @author: weijie.chen
 * @date:2019/10/10
 */
public class AutoJdBanksStatusUpdateJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoJdBanksStatusUpdateJob.class);

    @Resource
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Resource
    private JDZPUtil jdzpUtil;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("===========>任务开始执行-银行卡认证状态修改");
        QueryWrapper<CompanyBankAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_status", JdVerifiedEnum.AccountStatus.status2.getStatus());
        queryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        List<CompanyBankAccount> bankAccountList = companyBankAccountMapper.selectList(queryWrapper);
        if(bankAccountList.isEmpty()){
            logger.info("===========>没有要修改的银行卡");
            return;
        }
        logger.info("===========>银行卡认证状态修改列表,size:{},content:{}",bankAccountList.size(), bankAccountList);
        Set<Long> companyIds = bankAccountList.stream().map(CompanyBankAccount::getCompanyId).collect(Collectors.toSet());
        logger.info("===========>公司ID集合："+companyIds);
        companyIds.forEach( companyId -> {
            List<UserAccountInfoData> accountList = jdzpUtil.queryUserBindAccounts(companyId+"", 1).getData().getAccountList();
            accountList.forEach(userAccountInfoData -> {
                if(userAccountInfoData.getStatus() != 1){
                    Optional<CompanyBankAccount> optional = bankAccountList.stream().filter(companyBankAccount -> companyBankAccount.getBankAccountNo().equals(userAccountInfoData.getAccountNo())).findFirst();
                    if(optional.isPresent()){
                        CompanyBankAccount element = optional.get();
                        element.setAccountStatus(JdVerifiedEnum.AccountStatus.status4.getStatus());
                        element.setEcdsType(JdVerifiedEnum.EcdsType.type_0.getStatus());
                        element.setUpdateTime(LocalDateTime.now());
                        companyBankAccountMapper.updateById(element);
                        logger.info("===========>任务执行成功-银行卡认证状态修改，修改的银行ID为："+ element.getId());
                    }
                }
            });
        });
        logger.info("===========>任务执行结束-银行卡认证状态修改");
    }
}
