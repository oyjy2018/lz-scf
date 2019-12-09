package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.entity.Address;

/**
 * 地址管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:08
 *
 * @author liuchanghai
 * @create 2019-05-23 14:08
 * @since
 */
public interface AddressService extends IService<Address> {

    Result findlist();
}
