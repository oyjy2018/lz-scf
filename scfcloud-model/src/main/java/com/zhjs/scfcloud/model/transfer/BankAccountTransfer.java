package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddBankAccountDTO;
import com.zhjs.scfcloud.model.entity.CompanyBankAccount;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 银行卡数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 19:26
 *
 * @author liuchanghai
 * @create 2019-05-23 19:26
 * @since
 */

@Mapper(componentModel = "spring")
public interface BankAccountTransfer {

    List<CompanyBankAccount> addBankAccount2POs(List<AddBankAccountDTO> bankAccountList);

    CompanyBankAccount addBankAccount2PO(AddBankAccountDTO aba);
}
