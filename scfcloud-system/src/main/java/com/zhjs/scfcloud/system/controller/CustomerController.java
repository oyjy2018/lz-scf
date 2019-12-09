package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.IdCardDTO;
import com.zhjs.scfcloud.system.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户信息管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:37
 *
 * @author liuchanghai
 * @create 2019-06-11 15:37
 * @since
 */

@Api(tags = "客户信息管理")
@RestController
@RequestMapping("/customer/")
public class CustomerController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;


    @ApiOperation("保存用户信息")
    @PostMapping("save")
    public Result saveCustomer(@RequestBody CustomerDTO dto) {
        logger.info("保存用户信息{}" + dto.toString());
        return customerService.updateOrSaveByIdCard(dto);
    }

    @ApiOperation("获取用户信息")
    @PostMapping("findByIdCard")
    public Result findByIdCard(@RequestBody IdCardDTO dto) {
        logger.info("获取用户信息{}" + dto.toString());
        return customerService.findByIdCard(dto.getIdCard());
    }

    @ApiOperation("删除用户信息")
    @PostMapping("delete")
    public Result deleteByIdCard(@RequestBody BaseIdDTO dto) {
        logger.info("删除用户信息{}" + dto.toString());
        return customerService.deleteById(dto.getId());
    }
}
