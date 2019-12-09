package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyBankListDTO;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import com.zhjs.scfcloud.model.vo.CompanyBankSaveVO;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;

/**
 *
 * @author weijie.chen
 */
public interface CompanyBankAccountService extends IService<CompanyBankAccount> {
    /**
     * 银行列表
     * @param companyBankListDTO
     * @return
     */
    IPage<CompanyBankAccount> selectCompanyBanks(CompanyBankListDTO companyBankListDTO);

    /**
     * 新增银行卡
     * @param companyBankSaveVO
     * @return
     */
    Result insertBank(CompanyBankSaveVO companyBankSaveVO);

    /**
     * 重新发起小额打款
     * @param companyBankId
     * @return
     */
    Result paymentBank(Long companyBankId);

    /**
     * 小额打款认证
     * @param companyBankVerifyVO
     * @return
     */
    Result verifyBank(CompanyBankVerifyVO companyBankVerifyVO);

    /**
     * 设为默认账户
     * @param companyBankId
     * @param accountType
     * @return
     */
    Result setDefault(Long companyBankId,String accountType);

    /**
     * 修改账户类型
     * @param companyBankId
     * @param accountType
     * @return
     */
    Result deleteBankAccount(Long companyBankId,String accountType);
}

