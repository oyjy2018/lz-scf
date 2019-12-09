package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorAddDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorListDTO;
import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorUpdateDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.AcceptorService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
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
 * 承兑方 相关
 */
@Api("承兑方controller")
@RestController
@RequestMapping("/acceptor/")
public class AcceptorController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AcceptorService acceptorService;

    @ApiOperation("承兑方列表")
    @RequiresPermissions("ticket:acceptor:list")
    @PostMapping("list")
    public Result list(@RequestBody AcceptorListDTO dto){
        logger.info("subject:{},dto:{}","承兑方列表",dto.toString());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setCompanyId(user.getCompanyId());
        return acceptorService.findList(dto);
    }

    @ApiOperation("承兑方详情")
    @PostMapping("detail")
    public Result detail(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","承兑方详情",dto.toString());

        if (StringUtil.isEmpty(dto.getId())) return Result.failure("承兑方id为空");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        return acceptorService.detail(dto.getId(),user.getCompanyId());
    }

    @ApiOperation("新增承兑方")
    @RequiresPermissions("ticket:acceptor:add")
    @PostMapping("add")
    public Result add(@RequestBody @Valid AcceptorAddDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","新增承兑方",dto.toString());

        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setCompanyId(user.getCompanyId());
        dto.setUserId(user.getId());

        return acceptorService.add(dto);
    }

    @ApiOperation("删除承兑方")
    @RequiresPermissions("ticket:acceptor:delete")
    @PostMapping("delete")
    public Result delete(@RequestBody @Valid BaseDeleteDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","删除承兑方",dto.toString());

        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setUserId(user.getId());

        return acceptorService.delete(dto);
    }

    @ApiOperation("修改承兑方")
    @RequiresPermissions("ticket:acceptor:update")
    @PostMapping("update")
    public Result update(@RequestBody @Valid AcceptorUpdateDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","修改承兑方",dto.toString());

        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        dto.setUserId(user.getId());

        return acceptorService.update(dto);
    }

    @ApiOperation("修改承兑方限制")
    @RequiresPermissions("ticket:acceptor:updateLimit")
    @PostMapping("updateLimit")
    public Result updateLimit(@RequestBody BaseSingleUpdateDTO dto, BindingResult result){
        logger.info("subject:{},dto:{}","修改承兑方限制",dto.toString());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");
        dto.setId(user.getCompanyId());
        dto.setUpdateBy(user.getId());

        return acceptorService.updateLimit(dto);
    }

    @ApiOperation("查询当前承兑方限制")
    @PostMapping("isLimit")
    public Result isLimit(){
        logger.info("subject:{}","查询当前承兑方限制");

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息");

        return acceptorService.isLimit(user.getCompanyId());
    }
}
