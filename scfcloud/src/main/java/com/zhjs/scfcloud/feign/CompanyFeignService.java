package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dailongting
 * @date:2019/5/24 13:48
 * contextId: 这个属性可以在feign指向同一个服务时，用来区分他们不是同一个bean，也可以用spring:main:allow-bean-definition-overriding: true
 * 来设置允许不同的bean指向同一个服务，否则启动报错（由于使用配置中心的时候，配置文件配置上面的属性无效，故在类上指定contextID属性
 */
@FeignClient(value = "scfcloud-system",contextId = "company")
public interface CompanyFeignService {

    @ApiOperation("公司管理列表")
    @PostMapping("/company/find/list")
    Result findList(@RequestBody FindCompanyListDTO dto);

    @ApiOperation("公司详情")
    @GetMapping("/company/{companyId}")
    Result detail(@PathVariable("companyId") Long companyId);

    @ApiOperation("编辑公司状态")
    @PostMapping("/company/edit/status")
    Result editCompanyStatus(@RequestBody EditStatusDTO dto);

    @ApiOperation("查询用户加入的公司")
    @PostMapping("/company/findCompanyListByUserId")
    Result findCompanyListByUserId(@RequestBody UserIdAndCompanyIdDTO dto);

    @ApiOperation("删除公司")
    @PostMapping("/company/delete")
    Result deleteById(@RequestBody BaseIdDTO dto);

    //是否弹出邀请成员引导(只有一个成员时弹出)
    @PostMapping("/company/isInvite")
    Result isInvite(@RequestParam("companyId") Long companyId);

    @ApiOperation("京东实名认证链接")
    @GetMapping("/company/realUrl/{companyId}")
    Result realNameUrl(@PathVariable("companyId") Long companyId);

    @ApiOperation("基本信息")
    @GetMapping("/company/information/{companyId}")
    Result basicInformation(@PathVariable("companyId") Long companyId);

    @ApiOperation("是否京东实名认证")
    @GetMapping("/company/isJdVerified")
    Result isJdVerified(@RequestParam("companyId") Long companyId);
}
