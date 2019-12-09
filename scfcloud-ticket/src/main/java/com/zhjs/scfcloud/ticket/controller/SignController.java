package com.zhjs.scfcloud.ticket.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.esign.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.ticket.service.SignService;
import com.zhjs.scfcloud.ticket.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: dailongting
 * @date:2019/7/9 15:57
 */
@RestController
@RequestMapping("/sign/")
public class SignController {
    @Autowired
    private SignService signService;

    private Logger logger = LoggerFactory.getLogger(SignController.class);

    //签合同前发送验证码
    @PostMapping("sendCode")
    public Result sendCode(@RequestBody @Valid EsignSendCodeDTO esignSendCodeDTO, BindingResult result){
        logger.info("subject:{},dto:{}","签合同前发送验证码",esignSendCodeDTO);
        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        return signService.sendSmsCode(esignSendCodeDTO,user);
    }

    //获取合同内容
    @PostMapping("getContractContent")
    public Result getContractContent(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","签合同前发送验证码",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("订单id为空");
        return signService.getContractContent(dto.getId());
    }

    //更换合同
    @PostMapping("changeContract")
    public Result changeContract(@RequestBody @Valid EsignContractChangeDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","更换合同",dto);
        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());
        return signService.changeContract(dto);
    }

    //合同签署
    @PostMapping("signContract")
    @RequiresPermissions(value = {"ticket:contract:sign","ticket:contract:sign:quotation"},logical= Logical.OR)
    public Result signContract(@RequestBody @Valid EsignContractSignDTO esignContractSignDTO,BindingResult result){
        logger.info("subject:{},dto:{}","签合同前发送验证码",esignContractSignDTO);
        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        return signService.signContract(esignContractSignDTO,user);
    }

    //电签企业注册
    @PostMapping("createOrganize")
    public String createOrganize(@RequestBody EsignOrganizeCreateDTO dto){
        logger.info("subject:{},dto:{}","电签企业注册",dto);
        return signService.createOrganize(dto).toJSON();
    }

    //电签个人注册
    @PostMapping("createPerson")
    public String createPerson(@RequestBody EsignPersonCreateDTO dto){
        logger.info("subject:{},dto:{}","电签个人注册",dto);
        return signService.createPerson(dto).toJSON();
    }

    //校验关键字是否唯一
    @PostMapping("isOnlyKeyWord")
    public Result isOnlyKeyWord(@RequestBody @Valid EsignKeyWordOnlyValidDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","校验关键字是否唯一",dto);
        if (result.hasFieldErrors()) return Result.failure(result.getFieldError().getDefaultMessage());
        return signService.isOnlyKeyWord(dto);
    }

    //获取初始合同预览地址
    @PostMapping("getInitContractViewUrl")
    public Result getInitContractViewUrl(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","获取初始合同预览地址",dto);
        if (StringUtil.isEmpty(dto.getId())) return Result.failure("订单id为空");
        return signService.getInitContractViewUrl(dto.getId());
    }

}
