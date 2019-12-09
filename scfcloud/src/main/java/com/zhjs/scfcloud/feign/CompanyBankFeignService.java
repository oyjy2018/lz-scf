package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyBankListDTO;
import com.zhjs.scfcloud.model.vo.CompanyBankDefaultVO;
import com.zhjs.scfcloud.model.vo.CompanyBankSaveVO;
import com.zhjs.scfcloud.model.vo.CompanyBankVerifyVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 公司银行账户
 */

@FeignClient(value = "scfcloud-system", contextId = "companyBank")
public interface CompanyBankFeignService {

    @ApiOperation("查询公司是否已实名认证成功")
    @GetMapping("/company/{companyId}/jd/isReal")
    Result isJdReal(@PathVariable Long companyId);

    @ApiOperation("查询公司银行列表")
    @PostMapping("/company/{companyId}/bank")
    Result banks(@PathVariable Long companyId, @RequestBody CompanyBankListDTO companyBankListDTO);

    @ApiOperation("新增银行账户")
    @PostMapping("/company/bank")
    Result save(@RequestBody CompanyBankSaveVO companyBankSaveVO);

    @ApiOperation("重新发起小额打款")
    @PostMapping("/company/bank/{companyBankId}/payment")
    Result paymentBank(@PathVariable Long companyBankId);

    @ApiOperation("小额打款认证")
    @PostMapping("/company/bank/verify")
    Result verify(@RequestBody CompanyBankVerifyVO companyBankVerifyVO);

    @ApiOperation("设为默认账户")
    @PostMapping("/company/bank/{companyBankId}/default")
    Result bankDefault(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO);

    @ApiOperation("删除账户")
    @DeleteMapping("/company/bank/{companyBankId}")
    Result deleteAccountType(@PathVariable Long companyBankId,@RequestBody CompanyBankDefaultVO companyBankDefaultVO);
}
