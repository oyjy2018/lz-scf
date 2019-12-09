package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CompanyBankFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyBankListDTO;
import com.zhjs.scfcloud.model.vo.CompanyBankDefaultVO;
import com.zhjs.scfcloud.model.vo.CompanyBankSaveVO;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公司银行卡 Controller
 * @author weijie.chen
 */
@RestController
public class CompanyBankController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyBankFeignService companyBankFeignService;

    @ApiOperation("查询公司是否已实名认证成功")
    @GetMapping("/company/jd/isReal")
    public Result isJdReal(){
        logger.info("查询公司是否已实名认证成功");
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyBankFeignService.isJdReal(user.getCompanyId());
    }

    @ApiOperation("查询公司银行列表-收款账户")
    @GetMapping("/company/bank")
    @RequiresPermissions("common:companyBank:list")
    public Result banks(CompanyBankListDTO companyBankListDTO){
        logger.info("查询公司收款银行列表:param={}",companyBankListDTO);
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyBankFeignService.banks(user.getCompanyId(),companyBankListDTO);
    }

    @ApiOperation("查询公司银行列表-收票账户")
    @GetMapping("/company/bank/receipt")
    @RequiresPermissions("common:companyBank:receipt:list")
    public Result receiptBanks(CompanyBankListDTO companyBankListDTO){
        logger.info("查询公司收票银行列表:param={}",companyBankListDTO);
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyBankFeignService.banks(user.getCompanyId(),companyBankListDTO);
    }

    @ApiOperation("新增银行账户-收款账户")
    @PostMapping("/company/bank")
    @RequiresPermissions("common:companyBank:add")
    public Result save(@RequestBody @Validated CompanyBankSaveVO companyBankSaveVO, BindingResult bindingResult){
        logger.info("新增收款银行账户:{}", companyBankSaveVO.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
//        if(companyBankSaveVO.getAccountType() != CompanyBankAccountTypeEnum.type_2.getValue()){
//            if(!"平安银行".equals(companyBankSaveVO.getBankName())){
//                return Result.failure("收票账户目前只支持平安银行，更多银行支持敬请期待");
//            }
//        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null){
            return Result.failure("无登录信息");
        }
        companyBankSaveVO.setCompanyId(user.getCompanyId());
        return companyBankFeignService.save(companyBankSaveVO);
    }

    @ApiOperation("新增银行账户-收票账户")
    @PostMapping("/company/bank/receipt")
    @RequiresPermissions("common:companyBank:receipt:add")
    public Result receiptBankSave(@RequestBody @Validated CompanyBankSaveVO companyBankSaveVO, BindingResult bindingResult){
        logger.info("新增收票银行账户:{}", companyBankSaveVO.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        companyBankSaveVO.setCompanyId(MySubjectUtil.getUser().getCompanyId());
        return companyBankFeignService.save(companyBankSaveVO);
    }

    @ApiOperation("重新发起小额打款-收款账户")
    @PostMapping("/company/bank/{companyBankId}/payment")
    @RequiresPermissions("common:companyBank:again")
    public Result paymentBankReceipt(@PathVariable Long companyBankId){
        logger.info("重新发起小额打款:{}", companyBankId.toString());
        return companyBankFeignService.paymentBank(companyBankId);
    }

    @ApiOperation("重新发起小额打款-收票账户")
    @PostMapping("/company/bank/{companyBankId}/payment/receipt")
    @RequiresPermissions("common:companyBank:receipt:again")
    public Result paymentBank(@PathVariable Long companyBankId){
        logger.info("重新发起小额打款:{}", companyBankId.toString());
        return companyBankFeignService.paymentBank(companyBankId);
    }

    @ApiOperation("小额打款认证-收款银行")
    @PostMapping("/company/bank/verify")
    @RequiresPermissions("common:companyBank:verify")
    public Result verify(@RequestBody @Validated CompanyBankVerifyVO companyBankVerifyVO,BindingResult bindingResult){
        logger.info("收款银行小额打款认证:{}", companyBankVerifyVO.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return companyBankFeignService.verify(companyBankVerifyVO);
    }

    @ApiOperation("小额打款认证-收票银行")
    @PostMapping("/company/bank/verify/receipt")
    @RequiresPermissions("common:companyBank:receipt:verify")
    public Result verifyReceipt(@RequestBody @Validated CompanyBankVerifyVO companyBankVerifyVO,BindingResult bindingResult){
        logger.info("收票银行小额打款认证:{}", companyBankVerifyVO.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return companyBankFeignService.verify(companyBankVerifyVO);
    }

    @ApiOperation("设为默认账户-收款银行")
    @PostMapping("/company/bank/{companyBankId}/default")
    @RequiresPermissions("common:companyBank:default")
    public Result bankDefault(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO){
        logger.info("设为默认收款账户:companyBankId={},accountType={}", companyBankId.toString(),companyBankDefaultVO.getAccountType());
        return companyBankFeignService.bankDefault(companyBankId,companyBankDefaultVO);
    }

    @ApiOperation("设为默认账户-收票银行")
    @PostMapping("/company/bank/{companyBankId}/default/receipt")
    @RequiresPermissions("common:companyBank:receipt:default")
    public Result bankDefaultReceipt(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO){
        logger.info("设为默认收票账户:companyBankId={},accountType={}", companyBankId.toString(),companyBankDefaultVO.getAccountType());
        return companyBankFeignService.bankDefault(companyBankId,companyBankDefaultVO);
    }

    @ApiOperation("删除账户-收款银行")
    @DeleteMapping("/company/bank/{companyBankId}")
    @RequiresPermissions("common:companyBank:accountType")
    public Result deleteAccountType(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO){
        logger.info("删除收款账户:companyBankId={},accountType={}", companyBankId.toString(),companyBankDefaultVO.getAccountType());
        return companyBankFeignService.deleteAccountType(companyBankId,companyBankDefaultVO);
    }

    @ApiOperation("删除账户-收票账户")
    @DeleteMapping("/company/bank/{companyBankId}/receipt")
    @RequiresPermissions("common:companyBank:receipt:accountType")
    public Result deleteAccountTypeReceipt(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO){
        logger.info("删除收票账户:companyBankId={},accountType={}", companyBankId.toString(),companyBankDefaultVO.getAccountType());
        return companyBankFeignService.deleteAccountType(companyBankId,companyBankDefaultVO);
    }
}
