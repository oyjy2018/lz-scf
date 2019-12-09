package com.zhjs.scfcloud.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.*;
import com.zhjs.scfcloud.model.vo.CompanyListVO;
import com.zhjs.scfcloud.model.vo.CompanySelectVO;
import com.zhjs.scfcloud.model.vo.UserVO;
import com.zhjs.scfcloud.system.service.CompanyCommonService;
import com.zhjs.scfcloud.system.service.CompanyJdVerifiedService;
import com.zhjs.scfcloud.system.service.CompanyService;
import com.zhjs.scfcloud.system.service.CompanyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司管理 控制器
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 17:51
 *
 * @author liuchanghai
 * @create 2019-05-21 17:51
 * @since
 */

@Api(tags = "公司管理")
@RestController
public class CompanyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyUserService companyUserService;
    @Autowired
    private CompanyJdVerifiedService companyJdVerifiedService;
    @Autowired
    private CompanyCommonService companyCommonService;

    @ApiOperation("公司管理列表")
    @PostMapping("/company/find/list")
    public Result findList(@RequestBody FindCompanyListDTO dto) {
        logger.info(dto.toString());
        Page<CompanyListVO> list = companyService.selectCompanyListPage(dto);
        return Result.success(list);
    }

    @ApiOperation("公司详情")
    @GetMapping("/company/{companyId}")
    public Result detail(@PathVariable Long companyId) {
        logger.info("查询公司详情："+companyId);
        return companyService.selectCompanyDetail(companyId);
    }


    @ApiOperation("查询公司的所有成员")
    @PostMapping("/company/find/user/list")
    public Result findCompanyUserListById(@RequestBody CompanyIdDTO dto) {
        logger.info(dto.toString());
        List<UserVO> list = companyService.findCompanyUserListById(dto.getCompanyId());
        return Result.success(list);
    }

    @ApiOperation("查询用户的数据权限")
    @PostMapping("/company/findCompanyListByUserId")
    public Result findUserDataPermissionList(@RequestBody UserIdAndCompanyIdDTO dto) {
        logger.info(dto.toString());
        List<CompanySelectVO> list = companyService.findUserDataPermissionList(dto.getUserId());
        return Result.success(list);
    }

    @ApiOperation("编辑公司的状态")
    @PostMapping("/company/edit/status")
    public Result editCompanyStatus(@RequestBody EditStatusDTO dto) {
        logger.info(dto.toString());
        boolean result = companyService.editComapnyStatusById(dto);
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("删除公司")
    @PostMapping("/company/delete")
    public Result deleteById(@RequestBody BaseIdDTO dto) {
        logger.info(dto.toString());
        boolean result = companyService.deleteById(dto.getId());
        if(result){
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("公司是否存在")
    @PostMapping("/company/isExist")
    public Result isExist(@RequestBody IsExistDTO dto) {
        logger.info("公司是否存在 " + dto.toString());
        return Result.success(companyService.isExist(dto.getName()));
    }

    //是否弹出邀请成员引导(只有一个成员时弹出)
    @PostMapping("/company/isInvite")
    public Result isInvite(@RequestParam Long companyId){
        logger.info("subject:{},companyId:{}","是否弹出邀请成员引导(只有一个成员时弹出)",companyId);
        return companyUserService.isInvite(companyId);
    }

    @ApiOperation("京东实名认证链接")
    @GetMapping("/company/realUrl/{companyId}")
    public Result realNameUrl(@PathVariable Long companyId){
        logger.info("京东实名认证链接,companyId:{}",companyId);
        Map<String,Object> map = new HashMap<>();
        if(companyCommonService.isJdRegister(companyId) == 0){
            map.put("success",false);
            map.put("message","请先完成京东注册");
        }else{
            map.put("success",true);
            map.put("message",companyJdVerifiedService.selectRealUrl(companyId));
        }
        return Result.success(map);
    }

    @ApiOperation("基本信息")
    @GetMapping("/company/information/{companyId}")
    public Result basicInformation(@PathVariable Long companyId){
        return Result.success(companyService.findCompanyBasicInformation(companyId));
    }

    @GetMapping("/company/isJdVerified")
    public Result isJdVerified(@RequestParam("companyId") Long companyId) {
        return companyService.isJdVerified(companyId);
    }

}
