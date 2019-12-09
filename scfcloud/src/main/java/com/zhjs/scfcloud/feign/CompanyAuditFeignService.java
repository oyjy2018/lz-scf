package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 公司审核
 * @author weijie.chen
 */

@FeignClient(value = "scfcloud-system", contextId = "companyAudit")
public interface CompanyAuditFeignService {

    @ApiOperation("公司审核列表")
    @PostMapping("/company/audit")
    Result selectCompanyAuditList(@RequestBody FindCompanyAuditListDTO dto);

    @ApiOperation("审核信息详情")
    @GetMapping("/company/audit/{companyAuditId}/detail")
    Result detail(@PathVariable Long companyAuditId);

    @ApiOperation("公司注册")
    @PostMapping("/company/register")
    Result register(@RequestBody CompanyRegDTO dto);

    @ApiOperation("异步注册京东账户")
    @PostMapping("/company/{companyId}/async/register/jd")
    Result asyncRegisterJd(@PathVariable Long companyId);

    @ApiOperation("异步注册E签宝个人账户")
    @PostMapping("/company/{companyId}/async/register/personEsign")
    Result personEsignAsyncRegister(@PathVariable Long companyId);

    @ApiOperation("异步注册E签宝企业账户")
    @PostMapping("/company/{companyId}/async/register/companyEsign")
    Result companyEsignAsyncRegister(@PathVariable Long companyId);

    @ApiOperation("公司审核")
    @PostMapping("/company/audit/{companyAuditId}/{auditUserId}")
    Result audit(@PathVariable Long companyAuditId,@PathVariable Long auditUserId,@RequestBody CompanyAuditDTO dto);

    @ApiOperation("公司激活")
    @PostMapping("/company/active/{companyAuditId}")
    Result active(@PathVariable Long companyAuditId);

    @ApiOperation("重发激活邮件")
    @PostMapping("/company/active/{companyAuditId}/email")
    Result activeEmail(@PathVariable Long companyAuditId);

}
