package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CompanyAuditFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CompanyAuditDTO;
import com.zhjs.scfcloud.model.dto.CompanyRegDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyAuditListDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.enums.CompanyAuditEnum;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公司审核 Controller
 * @author weijie.chen
 */
@RestController
public class CompanyAuditController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CompanyAuditFeignService companyAuditFeignService;

    @ApiOperation("公司审核列表")
    @PostMapping("/company/audit")
    @RequiresPermissions("risk:company:audit")
    public Result list(@RequestBody @Validated FindCompanyAuditListDTO dto,BindingResult bindingResult){
        logger.info("查询公司银行列表:{}", dto.toString());
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        return companyAuditFeignService.selectCompanyAuditList(dto);
    }

    @ApiOperation("审核信息详情")
    @GetMapping("/company/audit/{companyAuditId}/detail")
    @RequiresPermissions("risk:company:audit")
    public Result detail(@PathVariable Long companyAuditId){
        logger.info("审核信息详情:{}", companyAuditId.toString());
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        return companyAuditFeignService.detail(companyAuditId);
    }

    @ApiOperation("公司注册")
    @PostMapping("/company/register")
    public Result register(@RequestBody @Validated CompanyRegDTO dto,BindingResult bindingResult){
        logger.info("公司注册:{}", dto.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        if(dto.getLegalPersonName().length() > 10){
            return Result.failure("法人姓名最多为10个字符");
        }
        if(dto.getPorxyPersonName().length() > 10){
            return Result.failure("被授权人姓名最多为10个字符");
        }
        String password = dto.getPorxyPersonPassword();
        if (password.length() <8 || password.length() > 16){
            return Result.failure("密码为8-16位");
        }
        if (!StringUtil.matchPwdReg(password)){
            return Result.failure("密码必须包含大小写字母、数字、符号中至少2种");
        }

        if(dto.getAupisLep() == CommonEnum.WhetherEnum.NO.getStatus()){
            if(StringUtils.isEmpty(dto.getPorxyCommissionUrl())){
                return Result.failure("授权委托书不能为空");
            }
        }
        return companyAuditFeignService.register(dto);
    }

    @ApiOperation("异步注册京东账户")
    @PostMapping("/company/{companyId}/async/register/jd")
    public Result asyncRegisterJd(@PathVariable Long companyId){
        logger.info("异步注册京东账户:companyId={}", companyId);
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if(user.getId() != 1L) return Result.failure("当前账户无权发起异步注册");
        return companyAuditFeignService.asyncRegisterJd(companyId);

    }

    @ApiOperation("异步注册E签宝个人账户")
    @PostMapping("/company/{companyId}/async/register/personEsign")
    public Result personEsignAsyncRegister(@PathVariable Long companyId){
        logger.info("异步注册E签宝个人账户:companyId={}", companyId);
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if(user.getId() != 1L) return Result.failure("当前账户无权发起异步注册");
        return companyAuditFeignService.personEsignAsyncRegister(companyId);

    }

    @ApiOperation("异步注册E签宝企业账户")
    @PostMapping("/company/{companyId}/async/register/companyEsign")
    public Result companyEsignAsyncRegister(@PathVariable Long companyId){
        logger.info("异步注册E签宝企业账户:companyId={}", companyId);
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        if(user.getId() != 1L) return Result.failure("当前账户无权发起异步注册");
        return companyAuditFeignService.companyEsignAsyncRegister(companyId);

    }

    @ApiOperation("公司审核")
    @PostMapping("/company/audit/{companyAuditId}")
    @RequiresPermissions("risk:company:audit")
    public Result audit(@PathVariable Long companyAuditId,@RequestBody @Validated CompanyAuditDTO dto,BindingResult bindingResult){
        logger.info("公司审核:{}", dto.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        //
        if(dto.getStatus() == CompanyAuditEnum.Status.status1.getStatus()){
            if(StringUtils.isEmpty(dto.getCompanyCategory())){
                return Result.failure("公司类别不能为空");
            }
        }
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");

        return companyAuditFeignService.audit(companyAuditId,user.getId(),dto);
    }

    @ApiOperation("公司激活")
    @PostMapping("/company/active/{companyAuditId}")
    public Result active(@PathVariable Long companyAuditId){
        logger.info("公司激活:{}", companyAuditId.toString());
        return companyAuditFeignService.active(companyAuditId);
    }

    @ApiOperation("重发激活邮件")
    @PostMapping("/company/active/{companyAuditId}/email")
    public Result activeEmail(@PathVariable Long companyAuditId){
        logger.info("重发激活邮件:{}", companyAuditId.toString());
        return companyAuditFeignService.activeEmail(companyAuditId);
    }

}
