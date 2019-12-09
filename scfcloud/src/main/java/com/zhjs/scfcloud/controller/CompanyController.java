package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CompanyFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.EditStatusDTO;
import com.zhjs.scfcloud.model.dto.FindCompanyListDTO;
import com.zhjs.scfcloud.model.dto.UserIdAndCompanyIdDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dailongting
 * @date:2019/5/24 13:46
 */
@Api(tags = "公司管理")
@RestController
public class CompanyController {

    private Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyFeignService companyFeignService;

    @ApiOperation("公司管理列表")
    @PostMapping("/company/find/list")
    @RequiresPermissions("common:company:list")
    public Result findList(@RequestBody @Validated FindCompanyListDTO dto, BindingResult bindingResult) {
        logger.info("查询管理列表 " + dto.toString());
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        }

        return companyFeignService.findList(dto);
    }

    @ApiOperation("公司详情")
    @GetMapping("/company/{companyId}")
    public Result detail(@PathVariable Long companyId) {
        logger.info("查询公司详情："+companyId);
        return companyFeignService.detail(companyId);
    }

    @ApiOperation("编辑公司状态")
    @PostMapping("/company/edit/status")
    @RequiresPermissions("common:company:editStatus")
    public Result editStatus(@RequestBody EditStatusDTO dto) {
        logger.info("编辑公司状态 " + dto.toString());
        return companyFeignService.editCompanyStatus(dto);
    }

    @ApiOperation("查询用户加入的公司")
    @PostMapping("/company/find/user/company")
    public Result findCompanyList() {
        // 获取userId
        UserIdAndCompanyIdDTO dto = new UserIdAndCompanyIdDTO();
        dto.setUserId(MySubjectUtil.getUserId());
        logger.info("查询用户加入的公司 " + dto.toString());
        return companyFeignService.findCompanyListByUserId(dto);
    }

    @ApiOperation("删除公司")
    @PostMapping("/company/delete")
    @RequiresPermissions("common:company:delete")
    public Result delete(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        return companyFeignService.deleteById(dto);
    }

    @ApiOperation("是否弹出邀请成员引导(只有一个成员时弹出)")
    @PostMapping("/company/isInvite")
    public Result isInvite() {
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        logger.info("user.getCompanyId()={}",user.getCompanyId());
        return companyFeignService.isInvite(user.getCompanyId());
    }

    @ApiOperation("京东实名认证链接")
    @PostMapping("/company/realUrl")
    @RequiresPermissions("common:company:realUrl")
    public Result realNameUrl(){
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyFeignService.realNameUrl(user.getCompanyId());
    }

    @ApiOperation("基本信息")
    @GetMapping("/company/information")
    @RequiresPermissions("common:company:information")
    public Result basicInformation(){
        //获取用户信息
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("无登录信息");
        return companyFeignService.basicInformation(user.getCompanyId());
    }

}
