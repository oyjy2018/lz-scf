package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.CreditApplyFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.CreditApplyService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @author: dailongting
 * @date:2019/6/12 12:00
 */
@RestController
@RequestMapping("/creditApply/")
public class CreditApplyController {
    private Logger logger = LoggerFactory.getLogger(CreditApplyController.class);

    @Autowired
    private CreditApplyService creditApplyService;
    @Autowired
    private CreditApplyFeignService creditApplyFeignService;

    @ApiOperation("保存用户信息")
    @PostMapping("/doAudit")
    public String doAudit(@RequestBody CreditAuditDTO dto){
        return null;
    }

    @ApiOperation("授信申请存草稿") //每次保存一个业务字段
    @PostMapping("saveDraft")
    @RequiresPermissions("risk:credit:apply:saveDraft")
    public String saveDraft(@RequestBody @Valid CreditApplyDraftSaveDTO creditApplyDraftSaveDTO, BindingResult bindingResult){
        logger.info("subject:{},creditApplyDraftSaveDTO:{}","授信申请存草稿", JsonUtil.toJSON(creditApplyDraftSaveDTO));
        if (creditApplyDraftSaveDTO == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyDraftSaveDTO.setCompanyId(userInfoVO.getCompanyId());
        creditApplyDraftSaveDTO.setUserId(userInfoVO.getId());
        creditApplyDraftSaveDTO.setUserName(userInfoVO.getUserName());
        return creditApplyService.saveDraft(creditApplyDraftSaveDTO);
    }

    @ApiOperation("授信申请提交")
    @PostMapping("commit")
    @RequiresPermissions("risk:credit:apply:commit")
    public String commit(@RequestBody @Valid CreditApplyCommitDTO creditApplyCommitDTO, BindingResult bindingResult) {
        logger.info("subject:{},creditApplyCommitDTO:{}","授信申请提交",JsonUtil.toJSON(creditApplyCommitDTO));
        if (creditApplyCommitDTO == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyCommitDTO.setCompanyId(userInfoVO.getCompanyId());
        creditApplyCommitDTO.setUserId(userInfoVO.getId());
        creditApplyCommitDTO.setUserName(userInfoVO.getUserName());
        return creditApplyService.commit(creditApplyCommitDTO);
    }

    @ApiOperation("授信申请存文件")
    @PostMapping("saveFile")
    public String saveFile(@RequestBody @Valid CreditApplyFileSaveDTO creditApplyFileSaveDTO, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请存文件", JsonUtil.toJSON(creditApplyFileSaveDTO));
        if (creditApplyFileSaveDTO == null)
            return Result.failure("参数缺失").toJSON();
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.saveFile(creditApplyFileSaveDTO);
    }

    @ApiOperation("我的申请")
    @PostMapping("myApplyList")
    @RequiresPermissions("risk:credit:apply:myList")
    public String myApplyList(@RequestBody @Valid CreditApplyMyListQueryDTO creditApplyMyListQueryDTO, BindingResult bindingResult){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","我的申请", JsonUtil.toJSON(creditApplyMyListQueryDTO));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.myApplyList(creditApplyMyListQueryDTO);
    }

    @ApiOperation("授信审核提交")
    @PostMapping("auditCommit")
    @RequiresPermissions("risk:credit:apply:audit:commit")
    public String auditCommit(@RequestBody @Valid CommonAuditCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},CreditAuditCommitDTO:{}","授信审核提交", JsonUtil.toJSON(dto));
        if (dto == null){
            return Result.failure("参数缺失").toJSON();
        }
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        }
        // 设置审核人
        dto.setAuditPerson(MySubjectUtil.getUser().getUserName());
        dto.setAuditPersonId(MySubjectUtil.getUser().getId());
        return creditApplyService.auditCommit(dto);
    }

    @ApiOperation("补充资料")
    @PostMapping("fileCommit")
    @RequiresPermissions("risk:credit:apply:my:fileCommit")
    public String fileCommit(@RequestBody @Valid CommonAuditCommitDTO dto, BindingResult bindingResult){
        logger.info("subject:{},CreditAuditCommitDTO:{}","补充资料", JsonUtil.toJSON(dto));
        if (dto == null){
            return Result.failure("参数缺失").toJSON();
        }
        if (bindingResult.hasErrors()){
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        }
        // 设置审核人
        dto.setAuditPersonId(MySubjectUtil.getUser().getId());
        dto.setAuditPerson(MySubjectUtil.getUser().getUserName());
        return creditApplyService.auditCommit(dto);
    }

    @ApiOperation("所有申请")
    @PostMapping("allApplyList")
    @RequiresPermissions("risk:credit:apply:allList")
    public String allApplyList(@RequestBody @Valid CreditApplyAllListQueryDTO creditApplyAllListQueryDTO){
        logger.info("subject:{},creditApplyFileSaveDTO:{}","所有申请", JsonUtil.toJSON(creditApplyAllListQueryDTO));
        UserInfoVO userInfoVO = MySubjectUtil.getUser();
        if (userInfoVO == null)
            return Result.failure("未获取到登录信息").toJSON();
        creditApplyAllListQueryDTO.setPermissionType(userInfoVO.getPermissionType());
        creditApplyAllListQueryDTO.setPermissionOrgIds(userInfoVO.getPermissionOrgIds());
        creditApplyAllListQueryDTO.setUserId(userInfoVO.getId());
        creditApplyAllListQueryDTO.setCompanyId(userInfoVO.getCompanyId());
        creditApplyAllListQueryDTO.setRoleIds(userInfoVO.getRoleList().stream().map(role -> role.getId().toString()).collect(Collectors.joining(",")));
        return creditApplyService.allApplyList(creditApplyAllListQueryDTO);
    }

    @ApiOperation("审核页面信息查询（授信详情、审核配置）")
    @PostMapping("getTrailingWorkFlowCfg")
    public String getTrailingWorkFlowCfg(@RequestBody CreditApplyQueryDTO dto){
        logger.info("subject:{},dto:{}","审核页面信息查询（授信详情、审核配置）", JsonUtil.toJSON(dto));
        long i = System.currentTimeMillis();
        logger.info("=================接口开始时间========"+ i);
        Result s = creditApplyService.getTrailingWorkFlowCfg(dto);
        logger.info("=================接口结束时间========"+ (i - System.currentTimeMillis()));
        return s.toJSON();
    }

    @ApiOperation("查询申请详情")
    @PostMapping("getDetail")
    @RequiresPermissions("risk:credit:apply:detail")
    public String getDetail(@RequestBody CreditApplyQueryDTO dto){
        return creditApplyService.getDetail(dto).toJSON();
    }

    @ApiOperation("查询授信申请文件资料")
    @PostMapping("document")
    @RequiresPermissions("risk:credit:apply:document")
    public String document(@RequestBody CreditApplyQueryDTO dto){
        return creditApplyFeignService.documents(dto).toJSON();
    }

    @ApiOperation("授信审核日记")
    @PostMapping("/findAuditLogList")
    @RequiresPermissions("risk:credit:apply:audit:log")
    public String findAuditLogList(@RequestBody @Valid AuditLogListDTO dto,BindingResult bindingResult) {
        logger.info("subject:{},AuditLogListDTO:{}","授信审核日记",dto.toString());
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.findAuditLogList(dto);
    }

    @ApiOperation("操作日志")
    @PostMapping("/findOperateLogList")
    @RequiresPermissions("risk:credit:apply:log")
    public String findOperateLogList(@RequestBody @Valid OperateLogDTO dto,BindingResult bindingResult) {
        logger.info("subject:{},findOperateLogList:{}","操作日志",dto.toString());
        if (bindingResult.hasErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return creditApplyService.findOperateLogList(dto);
    }

    @ApiOperation("授信审批列表")
    @PostMapping("auditList")
    @RequiresPermissions("risk:credit:apply:audit:list")
    public String auditList(@RequestBody CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO){
        logger.info("subject:{},creditApplyAuditListQueryDTO:{}","授信审批列表", JsonUtil.toJSON(creditApplyAuditListQueryDTO));
        return creditApplyService.auditList(creditApplyAuditListQueryDTO);
    }


    @ApiOperation("删除授信项目")
    @PostMapping("deleteCreditItem")
    //@RequiresPermissions("sxgl:credit:audit:list")
    public String deleteCreditItem(@RequestBody @Valid CreditItemDeleteDTO creditItemDeleteDTO,BindingResult result){
        logger.info("subject:{},creditItemDeleteDTO:{}","删除授信项目", JsonUtil.toJSON(creditItemDeleteDTO));
        if (result.hasErrors()) return result.getFieldError().getDefaultMessage();
        //return creditApplyService.auditList(creditApplyAuditListQueryDTO);
        return creditApplyService.deleteCreditItem(creditItemDeleteDTO);
    }

//    @GetMapping("/testIdcard")
    public String testIdcard(@RequestParam String idCard){
        UserInfoVO user = MySubjectUtil.getUser();
        System.out.println("oldIdCard="+user.getIdCard());
        user.setIdCard(idCard);
        MySubjectUtil.setUserInfoVO(user);
        System.out.println("newIdCard="+user.getIdCard());
        return idCard;
    }

    @ApiOperation("查询审核过程（最新）")
    @PostMapping("/findApprovalCourse")
    @RequiresPermissions("risk:credit:audit:course")
    public String findApprovalCourse(@RequestBody @Valid CreditApplyBaseDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","查询审核过程（最新）", JsonUtil.toJSON(dto));
        if (result.hasErrors()) return result.getFieldError().getDefaultMessage();
        return creditApplyService.findApprovalCourse(dto).toJSON();
    }

    @ApiOperation("查询授信申请详情（最新）")
    @PostMapping("/getDetails")
    @RequiresPermissions("risk:credit:apply:detail")
    public String getDetails(@RequestBody @Valid QueryApplyDetailsDTO dto,BindingResult result){
        logger.info("subject:{},dto:{}","查询授信申请详情（最新）", JsonUtil.toJSON(dto));
        if (result.hasErrors()) return result.getFieldError().getDefaultMessage();
        return creditApplyService.getDetails(dto).toJSON();
    }

}
