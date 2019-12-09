package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.entity.Customer;

/**
 *  用户信息管理的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:08
 *
 * @author liuchanghai
 * @create 2019-06-11 15:08
 * @since
 */

public interface CustomerService extends IService<Customer> {

    /**
     * 更新或保存用户信息 根据idCard 判断用户是否已存在
     * @param dto 入参
     * @return
     */
    Result updateOrSaveByIdCard(CustomerDTO dto);

    /**
     * 查询用户信息
     * @param idCard 身份证号
     * @return
     */
    Result findByIdCard(String idCard);

    /**
     * 删除用户信息
     * @param id 入参ID
     * @return
     */
    Result deleteById(Long id);
}
