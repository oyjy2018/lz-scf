package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CreditRecordFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordImportDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditRecordMyListQueryDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author: dailongting
 * @date:2019/6/12 12:00
 */
@RestController
@RequestMapping("/creditRecord/")
public class CreditRecordController {
    private Logger logger = LoggerFactory.getLogger(CreditRecordController.class);

    @Resource
    private CreditRecordFeignService creditRecordFeignService;


    @ApiOperation("我的授信")
    @PostMapping("myList")
    @RequiresPermissions("risk:credit:record:myList")
    public String myList(@RequestBody CreditRecordMyListQueryDTO creditRecordMyListQueryDTO){
        logger.info("subject:{},creditRecordMyListQueryDTO:{}","我的授信", JsonUtil.toJSON(creditRecordMyListQueryDTO));
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditRecordMyListQueryDTO.setIdCard(user.getIdCard());
        creditRecordMyListQueryDTO.setCompanyId(user.getCompanyId());
        creditRecordMyListQueryDTO.setClient("web");
        return creditRecordFeignService.myList(creditRecordMyListQueryDTO);
    }

    @ApiOperation("所有授信")
    @PostMapping("allList")
    @RequiresPermissions("risk:credit:record:allList")
    public String allList(@RequestBody CreditRecordAllListQueryDTO creditRecordAllListQueryDTO){
        logger.info("subject:{},creditRecordMyListQueryDTO:{}","所有授信", JsonUtil.toJSON(creditRecordAllListQueryDTO));
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();

        creditRecordAllListQueryDTO.setIdCard(user.getIdCard());
        creditRecordAllListQueryDTO.setCompanyId(user.getLastCompanyId());
        creditRecordAllListQueryDTO.setPermissionType(user.getPermissionType());
        creditRecordAllListQueryDTO.setPermissionOrgIds(user.getPermissionOrgIds());
        return creditRecordFeignService.allList(creditRecordAllListQueryDTO);
    }

    @ApiOperation("导入授信")
    @PostMapping("importRecord")
    public Result importRecord(@RequestBody @Valid CreditRecordImportDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","导入授信", JsonUtil.toJSON(dto));
        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());
        UserInfoVO user = MySubjectUtil.getUser();

        if (user == null) return Result.failure("未获取到登录信息");
        dto.setCompanyId(user.getCompanyId());
        dto.setUserId(user.getId());

        return creditRecordFeignService.importRecord(dto);
    }

    @ApiOperation("删除授信")
    @PostMapping("deleteRecord")
    public Result deleteRecord(@RequestBody @Valid BaseDeleteDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","删除授信", JsonUtil.toJSON(dto));

        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setCompanyId(user.getCompanyId());

        return creditRecordFeignService.deleteRecord(dto);
    }

    @ApiOperation("查询授信剩余额度")
    @PostMapping("findBalance")
    public Result findCreditBalance(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","查询授信剩余额度",dto.toString());
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("授信id为空");
        return creditRecordFeignService.findBalance(dto.getId());
    }


}
