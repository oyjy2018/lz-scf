package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.app.service.CreditApplyService;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyDraftSaveDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyMyListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditApplyQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/creditApply/")
public class CreditApplyController {
    private Logger logger = LoggerFactory.getLogger(CreditApplyController.class);

    @Resource
    private CreditApplyService creditApplyService;


    @ApiOperation("授信申请存草稿") //每次保存一个业务字段
    @PostMapping("saveDraft")
    public String saveDraft(@RequestBody @Valid CreditApplyDraftSaveDTO creditApplyDraftSaveDTO, BindingResult bindingResult){
        logger.info("subject:{},creditApplyDraftSaveDTO:{}","授信申请存草稿", JsonUtil.toJSON(creditApplyDraftSaveDTO));
        if (creditApplyDraftSaveDTO == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();

        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyDraftSaveDTO.setUserId(user.getId());
        creditApplyDraftSaveDTO.setCompanyId(MySubjectUtil.getCompanyId());

        return creditApplyService.saveDraft(creditApplyDraftSaveDTO);
    }

    @ApiOperation("授信申请提交")
    @PostMapping("commit")
    public String commit(@RequestBody @Valid CreditApplyCommitDTO creditApplyCommitDTO, BindingResult bindingResult) {
        logger.info("subject:{},creditApplyCommitDTO:{}","授信申请提交",JsonUtil.toJSON(creditApplyCommitDTO));
        if (creditApplyCommitDTO == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();

        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyCommitDTO.setUserId(user.getId());
        creditApplyCommitDTO.setCompanyId(MySubjectUtil.getCompanyId());

        return creditApplyService.commit(creditApplyCommitDTO);
    }

    @ApiOperation("我的申请列表")
    @PostMapping("myApplyList")
    public String myApplyList(@RequestBody CreditApplyMyListQueryDTO creditApplyMyListQueryDTO, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","我的申请", JsonUtil.toJSON(creditApplyMyListQueryDTO));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyMyListQueryDTO.setUserId(user.getId());
        creditApplyMyListQueryDTO.setCompanyId(MySubjectUtil.getCompanyId());
        creditApplyMyListQueryDTO.setClient("app");
        return creditApplyService.myApplyList(creditApplyMyListQueryDTO);
    }

    @ApiOperation("获取详情")
    @PostMapping("getDetail")
    public String getDetail(@RequestBody CreditApplyQueryDTO dto){
        logger.info("subject:{},dto:{}","获取详情", JsonUtil.toJSON(dto));
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setCompanyId(MySubjectUtil.getCompanyId());
        return creditApplyService.getDetail(dto).toJSON();
    }

    @ApiOperation("app获取授信申请审批字段与附件配置")
    @PostMapping("getWorkFlowFileCfg")
    public String getWorkFlowFileCfg(@RequestBody CreditApplyQueryDTO dto){
        logger.info("subject:{},dto:{}","app获取授信申请审批字段与附件配置", JsonUtil.toJSON(dto));
        dto.setCompanyId(MySubjectUtil.getCompanyId());
        dto.setCompanyName(MySubjectUtil.getCompanyName());
        return creditApplyService.getWorkFlowFileCfg(dto).toJSON();
    }

    @ApiOperation("app授信审批提交")
    @PostMapping("appAuditCommit")
    public String appAuditCommit(@RequestBody @Valid CreditAuditCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","app授信审批提交", JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        User user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setAuditPersonId(user.getId());
        dto.setAuditPerson(user.getUserName());
        dto.setCompanyId(MySubjectUtil.getCompanyId());
        return creditApplyService.appAuditCommit(dto).toJSON();
    }

}
