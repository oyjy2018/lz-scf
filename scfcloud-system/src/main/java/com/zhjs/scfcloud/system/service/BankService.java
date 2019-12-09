package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.dto.AddBankDTO;
import com.zhjs.scfcloud.model.entity.Bank;
import com.zhjs.scfcloud.model.vo.BankListVO;

import java.util.List;

/**
 * 银行管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 15:29
 *
 * @author liuchanghai
 * @create 2019-05-27 15:29
 * @since
 */
public interface BankService extends IService<Bank> {

    boolean add(AddBankDTO dto);

    List<BankListVO> findList();
}
