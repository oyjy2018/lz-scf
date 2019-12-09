package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyBankListDTO;
import com.zhjs.scfcloud.model.vo.CompanyBankAccountTypeVO;
import com.zhjs.scfcloud.model.vo.CompanyBankDefaultVO;
import com.zhjs.scfcloud.model.vo.CompanyBankSaveVO;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;
import com.zhjs.scfcloud.system.service.CompanyBankAccountService;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公司银行卡 Controller
 * @author weijie.chen
 */
@RestController
public class CompanyBankController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyBankAccountService companyBankAccountService;
    @Autowired
    private CompanyCommonService companyCommonService;

    @ApiOperation("查询公司是否已实名认证成功")
    @GetMapping("/company/{companyId}/jd/isReal")
    public Result isJdReal(@PathVariable Long companyId){
        logger.info("查询公司是否已实名认证成功:companyId={}", companyId);
        Result result = new Result();
        result.setData(companyCommonService.isJdVerified(companyId));
        result.setCode(Result.RESULT_CODE_SUCCESS);
        return result;
    }

    @ApiOperation("查询公司银行列表")
    @PostMapping("/company/{companyId}/bank")
    public Result banks(@PathVariable Long companyId,@RequestBody CompanyBankListDTO companyBankListDTO){
        logger.info("查询公司银行列表:companyId={},param={}", companyId, companyBankListDTO);
        companyBankListDTO.setCompanyId(companyId);
        return Result.success(companyBankAccountService.selectCompanyBanks(companyBankListDTO));
    }

    @ApiOperation("新增银行账户")
    @PostMapping("/company/bank")
    public Result save(@RequestBody CompanyBankSaveVO companyBankSaveVO){
        logger.info("新增银行账户:{}", companyBankSaveVO.toString());
        return companyBankAccountService.insertBank(companyBankSaveVO);
    }

    @ApiOperation("小额打款认证")
    @PostMapping("/company/bank/verify")
    public Result verify(@RequestBody CompanyBankVerifyVO companyBankVerifyVO){
        logger.info("小额打款认证:{}", companyBankVerifyVO.toString());
        return companyBankAccountService.verifyBank(companyBankVerifyVO);
    }

    @ApiOperation("重新发起小额打款")
    @PostMapping("/company/bank/{companyBankId}/payment")
    public Result paymentBank(@PathVariable Long companyBankId){
        logger.info("小额打款认证:{}", companyBankId.toString());
        return companyBankAccountService.paymentBank(companyBankId);
    }

    @ApiOperation("设为默认账户")
    @PostMapping("/company/bank/{companyBankId}/default")
    public Result bankDefault(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO){
        logger.info("设为默认账户:companyBankId={},accountType={}", companyBankId.toString(),companyBankDefaultVO.getAccountType());
        return companyBankAccountService.setDefault(companyBankId,companyBankDefaultVO.getAccountType());
    }

    @ApiOperation("删除账户")
    @DeleteMapping("/company/bank/{companyBankId}")
    public Result deleteAccountType(@PathVariable Long companyBankId,@RequestBody CompanyBankAccountTypeVO companyBankAccountTypeVO){
        logger.info("修改账户类型:companyBankId={},accountType={}", companyBankId.toString(),companyBankAccountTypeVO.getAccountType());
        return companyBankAccountService.deleteBankAccount(companyBankId,companyBankAccountTypeVO.getAccountType());
    }
}
