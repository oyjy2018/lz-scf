package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.AuditService;
import com.zhjs.scfcloud.service.CreditTicketApplyService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.ApiOperation;
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
 * @date:2019/6/28 17:01
 */
@RestController
@RequestMapping("/creditTicketApply")
public class CreditTicketApplyController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditTicketApplyService creditTicketApplyService;
    @Autowired
    private AuditService auditService;

    @ApiOperation("查询开票申请流程相关信息")
    @PostMapping("/findCreditTicketCfg")
    public String findCreditTicketCfg(@RequestBody @Valid QueryCreditTicketCfgDTO dto,BindingResult bindingResult){
        logger.info("查询开票申请流程相关信息:{}", JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.findCreditTicketCfg(dto).toJSON();
    }

    @ApiOperation("保存开票申请字段值")
    @PostMapping("/saveDraft")
    public String saveDraft(@RequestBody CreditTicketDraftSaveDTO dto){
        logger.info("保存开票申请字段值:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.saveDraft(dto).toJSON();
    }

    @ApiOperation("开商票审批提交")
    @PostMapping("/auditCommit")
    public String auditCommit(@RequestBody @Valid CommonAuditCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","app授信审批提交", JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null)
            return Result.failure("未获取到登录信息").toJSON();
        dto.setAuditPersonId(user.getId());
        dto.setAuditPerson(user.getUserName());

        //审核参数校验
        Result validResult = auditService.validParam(dto);
        if (validResult.getCode() == Result.RESULT_CODE_FAILURE)
            return validResult.toJSON();

        return creditTicketApplyService.auditCommit(dto).toJSON();
    }

    @ApiOperation("开票申请存文件")
    @PostMapping("/saveFile")
    public String saveFile(@RequestBody @Valid CreditTicketFileSaveDTO dto, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请存文件", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.saveFile(dto).toJSON();
    }

    /**
     * 开票申请提交
     * @param dto
     * @param bindingResult
     * @return
     */
    @PostMapping("/applyCommit")
    @RequiresPermissions("risk:creditTicket:applyCommit")
    public String applyCommit(@RequestBody CreditTicketApplyCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请存文件", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.applyCommit(dto).toJSON();
    }

    /**
     * 查询审核过程数据
     * @param dto
     * @param bindingResult
     * @return
     */
    @PostMapping("/findApprovalCourse")
    @RequiresPermissions("risk:credit:audit:course")
    public String findApprovalCourse(@RequestBody CreditTicketApplyBaseDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","查询审核过程数据", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.findApprovalCourse(dto).toJSON();
    }

    /**
     * 查询开票申请详情（新）
     * @param dto
     * @return
     */
    @PostMapping("/getDetails")
    public String getDetails(@RequestBody QueryApplyDetailsDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","查询开票申请详情（新）", JsonUtil.toJSON(dto));
        if (dto == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditTicketApplyService.getDetails(dto).toJSON();
    }

    @ApiOperation("审批完成列表")
    @PostMapping("/getAuditFinishList")
    @RequiresPermissions("risk:credit:use:aduditFinishList")
    public Result getAuditFinishList(@RequestBody CreditUseApplyAuditFinishListQueryDTO dto) {
        logger.info("审批完成列表:{}",JsonUtil.toSerializerJSON(dto));
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());
        return creditTicketApplyService.getAuditFinishList(dto);
    }

}
