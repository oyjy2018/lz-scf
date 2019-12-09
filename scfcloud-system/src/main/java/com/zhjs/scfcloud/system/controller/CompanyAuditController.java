package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import com.zhjs.scfcloud.model.vo.CompanyAuditBasicVO;
import com.zhjs.scfcloud.system.service.CompanyAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author weijie.chen
 */
@Api(tags = "公司审核controller")
@RestController
public class CompanyAuditController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyAuditService companyAuditService;


    @ApiOperation("公司审核列表")
    @PostMapping("/company/audit")
    public Result list(@RequestBody FindCompanyAuditListDTO dto) {
        logger.info("公司审核列表:{}", dto.toString());
        return Result.success(companyAuditService.selectCompanyAuditList(dto));
    }

    @ApiOperation("审核信息详情")
    @GetMapping("/company/audit/{companyAuditId}/detail")
    public Result<CompanyAuditBasicVO> detail(@PathVariable Long companyAuditId){
        logger.info("审核信息详情:{}", companyAuditId.toString());
        return Result.success(companyAuditService.selectCompanyAuditDetail(companyAuditId));
    }

    @ApiOperation("公司注册")
    @PostMapping("/company/register")
    public Result register(@RequestBody CompanyRegDTO dto){
        logger.info("公司注册:{}", dto.toString());
        try{
           return companyAuditService.companyRegister(dto);
        }catch (Exception e){
            logger.error("公司注册错误:{}", e.getMessage());
            return Result.failure("公司注册错误："+e.getMessage());
        }
    }

    @ApiOperation("异步注册京东账户")
    @PostMapping("/company/{companyId}/async/register/jd")
    public Result asyncRegister(@PathVariable Long companyId){
        logger.info("公司异步注册京东账户:{}", companyId);
        try{
            return companyAuditService.companyJdAsyncRegister(companyId);
        }catch (Exception e){
            logger.error("公司异步注册京东账户错误:{}", e.getMessage());
            return Result.failure("公司异步注册京东账户错误："+e.getMessage());
        }
    }

    @ApiOperation("异步注册E签宝个人账户")
    @PostMapping("/company/{companyId}/async/register/personEsign")
    public Result personEsignAsyncRegister(@PathVariable Long companyId){
        logger.info("异步注册E签宝个人账户:{}", companyId);
        try{
            return companyAuditService.personEsginAsyncRegister(companyId);
        }catch (Exception e){
            logger.error("异步注册E签宝个人账户:{}", e.getMessage());
            return Result.failure("异步注册E签宝个人账户："+e.getMessage());
        }
    }

    @ApiOperation("异步注册E签宝企业账户")
    @PostMapping("/company/{companyId}/async/register/companyEsign")
    public Result companyEsignAsyncRegister(@PathVariable Long companyId){
        logger.info("异步注册E签宝企业账户:{}", companyId);
        try{
            return companyAuditService.companyEsignAsyncRegister(companyId);
        }catch (Exception e){
            logger.error("异步注册E签宝企业账户:{}", e.getMessage());
            return Result.failure("异步注册E签宝企业账户："+e.getMessage());
        }
    }

    @ApiOperation("公司审核")
    @PostMapping("/company/audit/{companyAuditId}/{auditUserId}")
    public Result audit(@PathVariable Long companyAuditId,@PathVariable Long auditUserId,@RequestBody CompanyAuditDTO dto) {
        logger.info("公司审核:companyAuditId={},{}"+companyAuditId.toString() + dto.toString());
        return companyAuditService.companyAudit(companyAuditId,auditUserId,dto);
    }

    @ApiOperation("公司激活")
    @PostMapping("/company/active/{companyAuditId}")
    public Result active(@PathVariable Long companyAuditId) {
        logger.info("公司激活:companyAuditId={}"+companyAuditId.toString());
        return companyAuditService.companyActive(companyAuditId);
    }

    @ApiOperation("重发激活邮件")
    @PostMapping("/company/active/{companyAuditId}/email")
    public Result activeEmail(@PathVariable Long companyAuditId) {
        logger.info("公司激活:companyAuditId={}"+companyAuditId.toString());
        return companyAuditService.sendCompanyActiveEmail(companyAuditId);
    }

}
