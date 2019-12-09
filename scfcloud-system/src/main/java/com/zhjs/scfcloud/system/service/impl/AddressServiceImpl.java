package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.Address;
import com.zhjs.scfcloud.model.mapper.AddressMapper;
import com.zhjs.scfcloud.model.transfer.AddressTransfer;
import com.zhjs.scfcloud.system.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 地址管理业务逻辑接口的实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:08
 *
 * @author liuchanghai
 * @create 2019-05-23 14:08
 * @since
 */

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressTransfer addressTransfer;
    @Autowired
    private AddressMapper addressMapper;

    /**
     * 查询地址列表
     * @return
     */
    @Override
    public Result findlist() {
        return Result.success(addressMapper.findlist(1));
    }
}
