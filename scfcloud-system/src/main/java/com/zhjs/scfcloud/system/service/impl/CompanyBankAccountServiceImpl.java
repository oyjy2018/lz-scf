package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.jr.cbp.sdk.entity.billpay.request.ApplyAgencyDefpayReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.request.VerifyAgencyDefpayReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.ApplyAgencyDefpayRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.QueryUserBindAccountsRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.VerifyAgencyDefpayRespBody;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyBankListDTO;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.mapper.CompanyBankAccountMapper;
import com.zhjs.scfcloud.model.vo.CompanyBankSaveVO;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;
import com.zhjs.scfcloud.system.service.CompanyBankAccountService;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyBankAccountTypeEnum;
import com.zhjs.scfcloud.util.enums.JdVerifiedEnum;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weijie.chen
 */
@Service
public class CompanyBankAccountServiceImpl extends ServiceImpl<CompanyBankAccountMapper, CompanyBankAccount> implements CompanyBankAccountService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyBankAccountMapper companyBankAccountMapper;
    @Autowired
    private JDZPUtil jdzpUtil;

    @Override
    public IPage<CompanyBankAccount> selectCompanyBanks(CompanyBankListDTO companyBankListDTO) {
        QueryWrapper<CompanyBankAccount> bankAccountQueryWrapper = new QueryWrapper<>();
        bankAccountQueryWrapper.eq("company_id",companyBankListDTO.getCompanyId());
        bankAccountQueryWrapper.eq("is_delete",CommonEnum.WhetherEnum.NO.getStatus());
        bankAccountQueryWrapper.like("account_type",companyBankListDTO.getAccountType());
        bankAccountQueryWrapper.orderByDesc(CompanyBankAccountTypeEnum.type_1.getValue().equals(companyBankListDTO.getAccountType()) ? "is_receipt_default" : "is_repay_default");
        bankAccountQueryWrapper.orderByDesc("update_time");
        return companyBankAccountMapper.selectPage(new Page<>(companyBankListDTO.getCurrent(),companyBankListDTO.getSize()),bankAccountQueryWrapper);
    }

    @Override
    public Result insertBank(CompanyBankSaveVO companyBankSaveVO) {
        QueryWrapper<CompanyBankAccount> companyBankAccountQueryWrapper = new QueryWrapper<>();
        companyBankAccountQueryWrapper.eq("bank_account_no",companyBankSaveVO.getBankAccountNo());
        CompanyBankAccount checkBankAcount = companyBankAccountMapper.selectOne(companyBankAccountQueryWrapper);
        if(checkBankAcount == null){
            //发起京东打款
            ApplyAgencyDefpayReqBody reqBody = new ApplyAgencyDefpayReqBody();
            BeanUtils.copyProperties(companyBankSaveVO,reqBody);
            reqBody.setPlatformUserId(companyBankSaveVO.getCompanyId().toString());
            ApplyAgencyDefpayRespBody respBody = jdzpUtil.applyAgencyDefpay(reqBody).getData();
            //京东调用失败
            if(StringUtils.isEmpty(respBody.getSubStatus())){
                return Result.failure(respBody.getResponseDesc());
            }
            CompanyBankAccount record = new CompanyBankAccount();
            BeanUtils.copyProperties(companyBankSaveVO,record);
            //非默认收票账户
            record.setIsReceiptDefault(0);
            //非默认收款账户
            record.setIsRepayDefault(0);
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            //ECDS授权
            record.setEcdsType(JdVerifiedEnum.EcdsType.type_0.getStatus());
            //京东代付单Id
            record.setPayOrderId(respBody.getPayOrderId());
            //初始化状态
            if(respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_21.getStatus()) ||
                    respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_22.getStatus()) ||
                    respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_24.getStatus()) ||
                    respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_30.getStatus())){

                record.setAccountStatus(JdVerifiedEnum.AccountStatus.status3.getStatus());
            }else{
                record.setAccountStatus(JdVerifiedEnum.AccountStatus.status1.getStatus());

            }
            record.setSubStatus(respBody.getSubStatus());
            record.setIsDelete(CommonEnum.WhetherEnum.NO.getStatus());
            companyBankAccountMapper.insert(record);
            return Result.success("您的收款银行小额打款验证已经发起，请稍后回到该页面");
        }
        //银行卡已删除
        if(checkBankAcount.getIsDelete() == CommonEnum.WhetherEnum.YES.getStatus()){
            checkBankAcount.setIsDelete(CommonEnum.WhetherEnum.NO.getStatus());
            checkBankAcount.setAccountType(companyBankSaveVO.getAccountType());
            companyBankAccountMapper.updateById(checkBankAcount);
            return Result.success("新增"+CompanyBankAccountTypeEnum.getDesc(companyBankSaveVO.getAccountType())+"成功");
        }else {
            //银行卡未删除
            if (checkBankAcount.getAccountType().contains(companyBankSaveVO.getAccountType())){//相同类型的账户已存在
                return Result.failure("该"+CompanyBankAccountTypeEnum.getDesc(companyBankSaveVO.getAccountType())+"已存在,不可重复添加");
            }else{
                checkBankAcount.setAccountType(checkBankAcount.getAccountType()+","+companyBankSaveVO.getAccountType());
                companyBankAccountMapper.updateById(checkBankAcount);
                return Result.success("新增"+CompanyBankAccountTypeEnum.getDesc(companyBankSaveVO.getAccountType())+"成功");
            }
        }
    }

    @Override
    public Result paymentBank(Long companyBankId) {
        CompanyBankAccount companyBankAccount = companyBankAccountMapper.selectById(companyBankId);
        if(companyBankAccount.getAccountStatus() == JdVerifiedEnum.AccountStatus.status1.getStatus()){
            return Result.failure("当前银行卡在认证过程中，无需重新发起打款认证");
        }
        if(companyBankAccount.getAccountStatus() == JdVerifiedEnum.AccountStatus.status2.getStatus()){
            return Result.failure("当前银行卡已认证成功，无需重新发起打款认证");
        }
        //发起京东打款
        ApplyAgencyDefpayReqBody reqBody = new ApplyAgencyDefpayReqBody();
        reqBody.setPlatformUserId(companyBankAccount.getCompanyId().toString());
        BeanUtils.copyProperties(companyBankAccount,reqBody);
        ApplyAgencyDefpayRespBody respBody = jdzpUtil.applyAgencyDefpay(reqBody).getData();
        //京东调用失败
        if(StringUtils.isEmpty(respBody.getSubStatus())){
            return Result.failure(respBody.getResponseDesc());
        }
        //京东代付单Id
        companyBankAccount.setPayOrderId(respBody.getPayOrderId());
        //
        if(respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_22.getStatus()) ||
                respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_21.getStatus()) ||
                respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_24.getStatus()) ||
                respBody.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_30.getStatus())){

            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status3.getStatus());
        }else{
            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status1.getStatus());
        }
        companyBankAccount.setSubStatus(respBody.getSubStatus());
        companyBankAccount.setUpdateTime(LocalDateTime.now());

        companyBankAccountMapper.updateById(companyBankAccount);
        return Result.success();
    }

    @Override
    public Result verifyBank(CompanyBankVerifyVO companyBankVerifyVO) {
        CompanyBankAccount companyBankAccount = companyBankAccountMapper.selectById(companyBankVerifyVO.getCompanyBankId());
        if(companyBankAccount == null) return Result.failure("公司银行账户ID不存在，请确认参数");
        //状态校验
        if(companyBankAccount.getAccountStatus() == JdVerifiedEnum.AccountStatus.status2.getStatus()){
            return Result.failure("当前银行账户已完成认证，无需重复认证");
        }
        //打款校验
        if(companyBankAccount.getPaySuccessTime() == null){
            return Result.failure("尚未打款，不可进行打款认证");
        }

        Result result = new Result();
        //发起小额打款认证
        VerifyAgencyDefpayReqBody reqBody = new VerifyAgencyDefpayReqBody();
        reqBody.setPlatformUserId(companyBankAccount.getCompanyId().toString());
        reqBody.setPayOrderId(companyBankAccount.getPayOrderId());
        reqBody.setAmount(companyBankVerifyVO.getAmount().multiply(new BigDecimal(100)).longValue());
        VerifyAgencyDefpayRespBody body = jdzpUtil.verifyAgencyDefpay(reqBody).getData();
        logger.info("小额打款认证jd-logger："+body.toString());
        if(StringUtils.isEmpty(body.getSubStatus())) return Result.failure(body.getResponseDesc());
        //校验打款状态
        if(body.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_23.getStatus())){
            result.setMessage(JdVerifiedEnum.BankSubStatus.status_23.getDesc()+","+"剩余允许认证次数为"+body.getRemainConfirmFailNum()+"次");
            result.setCode(Result.RESULT_CODE_FAILURE);
            //剩余允许认证次数为0时，更改为认证失败
            if(body.getRemainConfirmFailNum() == 0){
                companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status3.getStatus());
            }
        }
        if(body.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_25.getStatus())){
            result.setMessage(JdVerifiedEnum.BankSubStatus.status_25.getDesc());
            result.setCode(Result.RESULT_CODE_FAILURE);
            //更改为认证失败
            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status3.getStatus());
        }
        if(body.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_40.getStatus())){
            if("1000".equals(body.getBindFlag())){
                result.setMessage(JdVerifiedEnum.BankSubStatus.status_40.getDesc()+",此卡已存在、重复绑卡");
            }else{
                result.setMessage(JdVerifiedEnum.BankSubStatus.status_40.getDesc()+",绑卡成功");
            }
            result.setCode(Result.RESULT_CODE_SUCCESS);
            //更改为认证成功
            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status2.getStatus());
            //如果未有默认卡，设为默认
            if(companyBankAccount.getAccountType().contains(CompanyBankAccountTypeEnum.type_2.getValue())){
                QueryWrapper<CompanyBankAccount> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("company_id",companyBankAccount.getCompanyId());
                queryWrapper1.eq("is_repay_default", CommonEnum.WhetherEnum.YES.getStatus());
                if(companyBankAccountMapper.selectCount(queryWrapper1) == 0){
                    companyBankAccount.setIsRepayDefault(1);
                }
            }
            if(companyBankAccount.getAccountType().contains(CompanyBankAccountTypeEnum.type_1.getValue())){
                QueryWrapper<CompanyBankAccount> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("company_id",companyBankAccount.getCompanyId());
                queryWrapper1.eq("is_receipt_default",CommonEnum.WhetherEnum.YES.getStatus());
                if(companyBankAccountMapper.selectCount(queryWrapper1) == 0){
                    companyBankAccount.setIsReceiptDefault(1);
                }
            }
            //查询京东银行信息绑定ID
            companyBankAccount.setBindId(selectBindId(companyBankAccount.getCompanyId(),companyBankAccount.getBankAccountNo()));
        }
        if(body.getSubStatus().equals(JdVerifiedEnum.BankSubStatus.status_50.getStatus())){
            result.setMessage(JdVerifiedEnum.BankSubStatus.status_50.getDesc());
            result.setCode(Result.RESULT_CODE_FAILURE);
            //更改为认证过期
            companyBankAccount.setAccountStatus(JdVerifiedEnum.AccountStatus.status4.getStatus());
        }

        companyBankAccount.setSubStatus(body.getSubStatus());
        companyBankAccount.setUpdateTime(LocalDateTime.now());

        companyBankAccountMapper.updateById(companyBankAccount);
        return result;
    }

    private String selectBindId(Long companyId,String accountNo){
        QueryUserBindAccountsRespBody body = jdzpUtil.queryUserBindAccounts(companyId.toString(), 1).getData();
        if(body.getAccountList().isEmpty()) return null;
        return body.getAccountList().stream().filter(userAccountInfoData -> userAccountInfoData.getAccountNo().equals(accountNo)).findFirst().orElse(null).getBindId();
    }

    @Override
    @Transactional
    public Result setDefault(Long companyBankId,String accountType) {
        CompanyBankAccount companyBankAccount = companyBankAccountMapper.selectById(companyBankId);
        if (companyBankAccount == null) return Result.failure("此账户不存在");
        if(companyBankAccount.getAccountStatus() != JdVerifiedEnum.AccountStatus.status2.getStatus()) return Result.failure("尚未认证成功，不可设为默认账户");
        if(!companyBankAccount.getAccountType().contains(accountType)){
            return Result.failure("当前账户不是" + CompanyBankAccountTypeEnum.getDesc(accountType) +",无法设为默认"+CompanyBankAccountTypeEnum.getDesc(accountType));
        }

        //修改已有默认账户
        QueryWrapper<CompanyBankAccount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id",companyBankAccount.getCompanyId());
        //判断类型
        if(accountType.equals(CompanyBankAccountTypeEnum.type_1.getValue())){
            //收票账户
            companyBankAccount.setIsReceiptDefault(CommonEnum.WhetherEnum.YES.getStatus());

            queryWrapper.eq("is_receipt_default",CommonEnum.WhetherEnum.YES.getStatus());
            CompanyBankAccount repayDefaultBank = companyBankAccountMapper.selectOne(queryWrapper);
            if(repayDefaultBank != null){
                repayDefaultBank.setIsReceiptDefault(CommonEnum.WhetherEnum.NO.getStatus());
                repayDefaultBank.setUpdateTime(LocalDateTime.now());
                companyBankAccountMapper.updateById(repayDefaultBank);
            }
        }else{
            //收款账户
            companyBankAccount.setIsRepayDefault(CommonEnum.WhetherEnum.YES.getStatus());

            queryWrapper.eq("is_repay_default",CommonEnum.WhetherEnum.YES.getStatus());
            CompanyBankAccount repayDefaultBank = companyBankAccountMapper.selectOne(queryWrapper);
            if(repayDefaultBank != null){
                repayDefaultBank.setIsRepayDefault(CommonEnum.WhetherEnum.NO.getStatus());
                repayDefaultBank.setUpdateTime(LocalDateTime.now());
                companyBankAccountMapper.updateById(repayDefaultBank);
            }
        }


        //更新当前账户
        companyBankAccount.setUpdateTime(LocalDateTime.now());
        companyBankAccountMapper.updateById(companyBankAccount);

        return Result.success();
    }

    @Override
    public Result deleteBankAccount(Long companyBankId, String accountType) {
        CompanyBankAccount companyBankAccount = getById(companyBankId);
        if(companyBankAccount == null || (!companyBankAccount.getAccountType().contains(accountType))) {
            return Result.failure("不存在此"+CompanyBankAccountTypeEnum.getDesc(accountType));
        }
        String[] types = companyBankAccount.getAccountType().split(",");
        if(types.length == 1){
            if(!types[0].equals(accountType)){
                return Result.failure("不存在此"+CompanyBankAccountTypeEnum.getDesc(accountType));
            }
            companyBankAccount.setIsDelete(CommonEnum.WhetherEnum.YES.getStatus());
            companyBankAccountMapper.updateById(companyBankAccount);
        }else{
            for (String type : types) {
                if(!type.equals(accountType)){
                    companyBankAccount.setAccountType(type);
                    companyBankAccountMapper.updateById(companyBankAccount);
                }
            }
        }
        return Result.success();
    }
}
