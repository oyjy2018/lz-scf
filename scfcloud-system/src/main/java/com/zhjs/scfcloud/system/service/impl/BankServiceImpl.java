package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.entity.Bank;
import com.zhjs.scfcloud.model.mapper.BankMapper;
import com.zhjs.scfcloud.model.transfer.BankTransfer;
import com.zhjs.scfcloud.model.vo.BankListVO;
import com.zhjs.scfcloud.model.dto.AddBankDTO;
import com.zhjs.scfcloud.system.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行管理业务逻辑接口的实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 15:29
 *
 * @author liuchanghai
 * @create 2019-05-27 15:29
 * @since
 */

@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

    @Autowired
    private BankTransfer bankTransfer;

    @Override
    public boolean add(AddBankDTO dto) {
        Bank bank = bankTransfer.addDTO2POs(dto);
        return save(bank);
    }

    @Override
    public List<BankListVO> findList() {
        List<Bank> list = list(null);
        List<BankListVO> reluts = bankTransfer.banks2BankListVO(list);
        return reluts;
    }
}
