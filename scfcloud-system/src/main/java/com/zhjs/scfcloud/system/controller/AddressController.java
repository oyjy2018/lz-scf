package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.system.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地址管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:11
 *
 * @author liuchanghai
 * @create 2019-05-23 14:11
 * @since
 */

@Api(tags = "地址管理")
@RestController
@RequestMapping("/address/")
public class AddressController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AddressService addressService;

    @ApiOperation("查询地址列表")
    @PostMapping("findlist")
    public Result findUserlist(){
        logger.info("查询地址列表");
        return addressService.findlist();
    }

}
