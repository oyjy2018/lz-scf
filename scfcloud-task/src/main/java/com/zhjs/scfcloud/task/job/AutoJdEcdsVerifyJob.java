package com.zhjs.scfcloud.task.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.jr.cbp.sdk.entity.billpay.common.AccountDetail;
import com.jd.jr.cbp.sdk.entity.billpay.request.QueryEcdsAuthReqDto;
import com.jd.jr.cbp.sdk.entity.billpay.response.QueryEcdsAuthRespDto;
import com.jd.jr.cbp.sdk.entity.billpay.response.QueryUserBindAccountsRespBody;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyBankAccountTypeEnum;
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

/**
 * ECDS认证定时器
 * @author: weijie.chen
 * @date:2019/7/26 11:48
 */
public class AutoJdEcdsVerifyJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(AutoJdEcdsVerifyJob.class);

    @Resource
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Resource
    private JDZPUtil jdzpUtil;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("===========>任务开始执行-ECDS认证");
        QueryWrapper<CompanyBankAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_status", JdVerifiedEnum.AccountStatus.status2.getStatus());
        queryWrapper.like("account_type", CompanyBankAccountTypeEnum.type_1.getValue());
        queryWrapper.eq("ecds_type", JdVerifiedEnum.EcdsType.type_0.getStatus());
        queryWrapper.eq("is_delete", CommonEnum.WhetherEnum.NO.getStatus());
        List<CompanyBankAccount> bankAccountList = companyBankAccountMapper.selectList(queryWrapper);
        if(bankAccountList.isEmpty()){
            logger.info("===========>没有要认证的银行卡");
            return;
        }
        logger.info("===========>ECDS认证银行卡列表,size:{},content:{}",bankAccountList.size(), bankAccountList);
        bankAccountList.forEach(companyBankAccount -> {
            //查询京东授权
            QueryEcdsAuthReqDto reqDto = new QueryEcdsAuthReqDto();
            AccountDetail accountDetail = new AccountDetail();
            accountDetail.setAccountNo(companyBankAccount.getBankAccountNo());
            accountDetail.setBankCode(companyBankAccount.getBankCode());
            reqDto.setBuyerAccount(accountDetail);
            QueryEcdsAuthRespDto respDto = jdzpUtil.queryEcdsAccountAuth(reqDto).getData();
            if("1".equals(respDto.getAuthFlag())){
                companyBankAccount.setEcdsType(JdVerifiedEnum.EcdsType.type_1.getStatus());
                //查询京东银行信息绑定ID
                QueryUserBindAccountsRespBody body = jdzpUtil.queryUserBindAccounts(companyBankAccount.getCompanyId().toString(), 1).getData();
                if(!body.getAccountList().isEmpty()){
                    String bindId = body.getAccountList().stream().filter(userAccountInfoData -> userAccountInfoData.getAccountNo().equals(companyBankAccount.getBankAccountNo())).findFirst().orElse(null).getBindId();
                    companyBankAccount.setBindId(body.getAccountList().stream().filter(
                            userAccountInfoData -> userAccountInfoData.getAccountNo().equals(companyBankAccount.getBankAccountNo())
                        ).findFirst().orElse(null).getBindId()
                    );
                }
                companyBankAccount.setUpdateTime(LocalDateTime.now());
                companyBankAccountMapper.updateById(companyBankAccount);
                logger.info("===========>任务执行成功-修改ECDS认证，修改的银行ID为："+ companyBankAccount.getId());
            }
        });
        logger.info("===========>任务执行结束-ECDS认证");
    }
}
