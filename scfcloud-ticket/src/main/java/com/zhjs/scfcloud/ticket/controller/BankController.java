package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.ticket.PermissionBankListDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.CompanyBankService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weijie.chen
 */
@Api("银行卡controller")
@RestController
public class BankController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CompanyBankService companyBankService;


    @ApiOperation("银行账户")
    @GetMapping("/company/bank")
    @RequiresPermissions("operationsMgt:companyBanks:view")
    public Result banks(PermissionBankListDTO dto){
        logger.info("subject:{},dto:{}","银行账户",dto.toString());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        return Result.success(companyBankService.findCompanyBanksByUserPermission(user.getId(),dto));
    }
}
