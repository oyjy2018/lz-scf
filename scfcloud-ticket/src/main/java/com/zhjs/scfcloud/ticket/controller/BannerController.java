package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerAddDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerListDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerSortDTO;
import com.zhjs.scfcloud.model.dto.banner.BannerUpdateDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.BannerService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * banner 相关
 */
@Api("banner controller")
@RestController
@RequestMapping("/banner/")
public class BannerController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BannerService bannerService;

    @ApiOperation("banner列表")
    @RequiresPermissions("operationsMgt:banner:list")
    @PostMapping("list")
    public Result list(@RequestBody BannerListDTO dto){
        logger.info("subject:{},dto:{}","banner列表",dto.toString());
        return bannerService.list(dto);
    }

    @ApiOperation("添加banner")
    @RequiresPermissions("operationsMgt:banner:add")
    @PostMapping("add")
    public Result add(@Valid @RequestBody BannerAddDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","添加banner",dto.toString());
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());

        return bannerService.add(dto);
    }

    @ApiOperation("删除banner")
    @RequiresPermissions("operationsMgt:banner:delete")
    @PostMapping("delete")
    public Result delete(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","删除banner",dto.toString());
        if (StringUtil.isEmpty(dto.getId())) {
            return Result.failure("id不能为空");
        }

        return bannerService.delete(dto.getId(),MySubjectUtil.getUserId());
    }

    @ApiOperation("启用/禁用banner")
    @RequiresPermissions("operationsMgt:banner:updateUseStatus")
    @PostMapping("updateUseStatus")
    public Result updateUseStatus(@Valid @RequestBody BaseSingleUpdateDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","启用/禁用banner",dto.toString());
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUpdateBy(user.getId());

        return bannerService.updateUseStatus(dto);
    }

    @ApiOperation("排序banner")
    @RequiresPermissions("operationsMgt:banner:sort")
    @PostMapping("sort")
    public Result sort(@Valid @RequestBody BannerSortDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","排序banner",dto.toString());
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());

        return bannerService.sort(dto);
    }

    @ApiOperation("banner详情")
    @GetMapping("detail")
    public Result detail(@RequestParam("id") Long id){
        logger.info("subject:{},id:{}","banner详情",id);
        return bannerService.detail(id);
    }

    @ApiOperation("修改banner")
    @RequiresPermissions("operationsMgt:banner:update")
    @PostMapping("update")
    public Result update(@Valid @RequestBody BannerUpdateDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","banner列表", JsonUtil.toSerializerJSON(dto));
        if (result.hasFieldErrors()) {
            return Result.failure(result.getFieldError().getDefaultMessage());
        }
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());

        return bannerService.update(dto);
    }

    /**
     * 首页获取轮播图
     * @return
     */
    @GetMapping("getCarousel")
    public Result getCarousel(){
        logger.info("subject:{}","首页获取轮播图");
        return bannerService.getCarousel();
    }

}
