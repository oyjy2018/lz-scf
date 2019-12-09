package com.zhjs.scfcloud.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.entity.Customer;
import com.zhjs.scfcloud.model.mapper.CustomerMapper;
import com.zhjs.scfcloud.model.transfer.CustomerTransfer;
import com.zhjs.scfcloud.model.vo.CustomerVO;
import com.zhjs.scfcloud.system.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 用户信息管理的业务逻辑接口的实现类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:10
 *
 * @author liuchanghai
 * @create 2019-06-11 15:10
 * @since
 */

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {



    @Autowired
    private CustomerTransfer customerTransfer;
    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Result updateOrSaveByIdCard(CustomerDTO dto) {
        LambdaQueryWrapper<Customer> lambda = new QueryWrapper<Customer>().lambda();
        lambda.eq(Customer::getIdCard, dto.getIdCard());
        Customer customer0 = getOne(lambda);
        Customer customer = customerTransfer.addDTO2Po(dto);
        customer.setUpdateTime(LocalDateTime.now());
        boolean result ;
        customer.setUpdateTime(LocalDateTime.now());
        customer.setUpdateBy(dto.getUserId());
        if (customer0 != null){
            customer.setUpdateBy(dto.getUserId());
            result = customerMapper.updateByIdCardSelective(customer);
        }else {
            customer.setCreateBy(dto.getUserId());
            customer.setCreateTime(LocalDateTime.now());
            customer.setIsDelete(0);
            result = save(customer);
        }

        if (result){
            return Result.success("用户信息保存成功");
        }
       return Result.failure("用户信息保存失败");
    }

    @Override
    public Result findByIdCard(String idCard) {
        LambdaQueryWrapper<Customer> lambda = new QueryWrapper<Customer>().lambda();
        lambda.eq(Customer::getIdCard, idCard);
        Customer customer = getOne(lambda);
        CustomerVO customerVO = customerTransfer.Po2CustomerVO(customer);
        return Result.success(customerVO);
    }

    @Override
    public Result deleteById(Long id) {
        if (removeById(id)){
            return Result.success("删除成功");
        }
        return Result.failure("删除失败");
    }
}
