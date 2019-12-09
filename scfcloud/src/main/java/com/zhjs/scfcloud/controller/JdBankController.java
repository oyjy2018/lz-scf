package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *  京东银行相关API
 * @author weijie.chen
 */

@Api(tags = "京东支持银行接口")
@RestController
public class JdBankController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JDZPUtil jdzpUtil;

    @ApiOperation("查询支持小额打款银行列表")
    @GetMapping("/jd/banks")
    public Result banks() {
        logger.info("查询支持小额打款银行列表");
        return Result.success(jdzpUtil.queryBankList().getData().getBankListResult());
    }

    @ApiOperation("查询开户行省")
    @GetMapping("/jd/bank/provinces")
    public Result provinces() {
        logger.info("查询开户行省");
        return Result.success(jdzpUtil.queryProvincesAndCitys(null).getData().getProvinces());
    }

    @ApiOperation("查询开户行市")
    @GetMapping("/jd/bank/{provinceId}/citys")
    public Result citys(@PathVariable String provinceId) {
        logger.info("查询开户行市");
        return Result.success(jdzpUtil.queryProvincesAndCitys(provinceId).getData().getCitys());
    }

    @ApiOperation("查询开户行支行列表")
    @GetMapping("/jd/{cnapBankCode}/{cityId}")
    public Result bankCnaps(@PathVariable Integer cnapBankCode,@PathVariable String cityId) {
        logger.info("查询开户行支行列表");
        return Result.success(jdzpUtil.queryBankCnapsList(cnapBankCode,cityId).getData().getBankChildInfoList());
    }
}
