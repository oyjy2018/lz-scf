package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.AddBankDTO;
import com.zhjs.scfcloud.model.entity.Bank;
import com.zhjs.scfcloud.model.vo.BankListVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 银行数据转换接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 16:03
 *
 * @author liuchanghai
 * @create 2019-05-27 16:03
 * @since
 */

@Mapper(componentModel = "spring")
public interface BankTransfer {
    
    List<BankListVO> banks2BankListVO(List<Bank> list);

    Bank addDTO2POs(AddBankDTO dto);
}
