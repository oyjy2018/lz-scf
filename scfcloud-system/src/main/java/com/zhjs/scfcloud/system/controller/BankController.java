package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AddBankDTO;
import com.zhjs.scfcloud.model.vo.BankListVO;
import com.zhjs.scfcloud.system.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 15:31
 *
 * @author liuchanghai
 * @create 2019-05-27 15:31
 * @since
 */

@Api(tags = "Api接口")
@RestController
@RequestMapping("bank")
public class BankController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BankService bankService;

    @ApiOperation("新建菜单")
    @PostMapping("add")
    public Result add(@RequestBody AddBankDTO dto) {
        logger.info(dto.toString());
        boolean result = bankService.add(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("查询菜单列表")
    @PostMapping("select/list")
    public Result findList() {
        logger.info("查询菜单列表");
        List<BankListVO> list = bankService.findList();
        return Result.success(list);
    }
}
