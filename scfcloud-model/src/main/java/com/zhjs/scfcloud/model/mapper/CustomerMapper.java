package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.entity.Customer;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:06
 *
 * @author liuchanghai
 * @create 2019-06-11 15:06
 * @since
 */
public interface CustomerMapper extends BaseMapper<Customer> {

    boolean updateByIdCardSelective(Customer customer);
}
