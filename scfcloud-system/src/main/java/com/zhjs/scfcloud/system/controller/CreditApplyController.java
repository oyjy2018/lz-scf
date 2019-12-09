package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.User;
import com.zhjs.scfcloud.system.service.CreditApplyService;
import com.zhjs.scfcloud.system.service.OperateLogService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授信申请相关
 */
@Api(tags = "授信管理")
@RestController
@RequestMapping("/creditApply")
public class CreditApplyController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private CreditApplyService creditApplyService;

    //授信申请保存草稿
    @PostMapping("/saveDraft")
    public String saveDraft(@RequestBody CreditApplyDraftSaveDTO creditApplyDraftSaveDTO){
        logger.info("subject:{},creditApplyDraftSaveDTO:{}","业务保存草稿",creditApplyDraftSaveDTO);
        return creditApplyService.saveDraft(creditApplyDraftSaveDTO);
    }

    //授信申请提交
    @PostMapping("/commit")
    public String commit(@RequestBody CreditApplyCommitDTO creditApplyCommitDTO){
        logger.info("subject:{},creditApplyCommitDTO:{}","授信申请提交",creditApplyCommitDTO);
        return creditApplyService.commit(creditApplyCommitDTO);
    }


    //授信申请保存文件
    @PostMapping("/saveFile")
    public String saveFile(@RequestBody CreditApplyFileSaveDTO creditApplyFileSaveDTO) {
        logger.info("subject:{},creditApplyFileSaveDTO:{}","授信申请保存文件",creditApplyFileSaveDTO);
        return creditApplyService.saveFile(creditApplyFileSaveDTO);
    }

    //授信申请我的申请列表
    @PostMapping("/myApplyList")
    public String myApplyList(@RequestBody CreditApplyMyListQueryDTO creditApplyMyListQueryDTO){
        logger.info("subject:{},creditApplyMyListQueryDTO:{}","授信申请我的申请列表",creditApplyMyListQueryDTO);
        return creditApplyService.myApplyList(creditApplyMyListQueryDTO);
    }

    @ApiOperation("授信审核提交")
    @PostMapping("/auditCommit")
    public Result<User> auditCommit(@RequestBody CommonAuditCommitDTO dto) {
        logger.info("subject:{},CreditAuditCommitDTO:{}","授信审核提交",dto.toString());
        return creditApplyService.auditCommit(dto);
    }

    @ApiOperation("授信审核日记")
    @PostMapping("/findAuditLogList")
    public String findAuditLogList(@RequestBody AuditLogListDTO dto) {
        logger.info("subject:{},AuditLogListDTO:{}","授信审核日记",dto.toString());
        return creditApplyService.findAuditLogList(dto);
    }

    @ApiOperation("查询授信申请详情")
    @PostMapping("/findCreditApplyDetails")
    public Result findCreditApplyDetails(@RequestBody CreditApplyQueryDTO dto){
        logger.info("subject:{},CreditAuditCommitDTO:{}","查询授信申请详情",dto);
        return creditApplyService.findCreditApplyDetails(dto);
    }

    @ApiOperation("查询授信申请文件资料")
    @PostMapping("/document")
    public Result documents(@RequestBody CreditApplyQueryDTO dto){
        logger.info("subject:{},CreditAuditCommitDTO:{}","查询授信申请文件资料",dto);
        return creditApplyService.findCreditApplyFile(dto);
    }

    //授信申请所有申请
    @PostMapping("/allApplyList")
    public String allApplyList(@RequestBody CreditApplyAllListQueryDTO creditApplyAllListQueryDTO){
        logger.info("subject:{},creditApplyAllListQueryDTO:{}","授信申请所有申请",creditApplyAllListQueryDTO);
        return creditApplyService.allApplyList(creditApplyAllListQueryDTO);
    }

    //授信审批列表
    @PostMapping("/auditList")
    public String auditList(@RequestBody CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO){
        logger.info("subject:{},creditApplyAuditListQueryDTO:{}","授信审批列表",creditApplyAuditListQueryDTO);
        return creditApplyService.auditList(creditApplyAuditListQueryDTO);
    }

    @ApiOperation("操作日志")
    @PostMapping("/findOperateLogList")
    public String findOperateLogList(@RequestBody OperateLogDTO dto){
        logger.info("subject:{},OperateLogDTO:{}","操作日志",dto);
        return operateLogService.findList(dto);
    }

    @ApiOperation("app授信审核提交")
    @PostMapping("/appAuditCommit")
    public Result appAuditCommit(@RequestBody com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO dto) {
        logger.info("subject:{},CreditAuditCommitDTO:{}","app授信审核提交",dto.toString());
        return creditApplyService.appAuditCommit(dto);
    }

    @ApiOperation("删除授信项目")
    @PostMapping("deleteCreditItem")
    //@RequiresPermissions("sxgl:credit:audit:list")
    public String deleteCreditItem(@RequestBody CreditItemDeleteDTO creditItemDeleteDTO){
        logger.info("subject:{},creditItemDeleteDTO:{}","删除授信项目", JsonUtil.toJSON(creditItemDeleteDTO));
        //return creditApplyService.auditList(creditApplyAuditListQueryDTO);
        return creditApplyService.deleteCreditItem(creditItemDeleteDTO);
    }

    @ApiOperation("是否有草稿")
    @PostMapping("hasDraft")
    public String hasDraft(@RequestBody CreditItemDeleteDTO creditItemDeleteDTO){
        logger.info("subject:{},creditItemDeleteDTO:{}","是否有草稿", JsonUtil.toJSON(creditItemDeleteDTO));
        //return creditApplyService.auditList(creditApplyAuditListQueryDTO);
        return creditApplyService.deleteCreditItem(creditItemDeleteDTO);
    }

    @ApiOperation("查询审核过程（最新）")
    @PostMapping("/findApprovalCourse")
    public Result findApprovalCourse(@RequestBody CreditApplyBaseDTO dto){
        logger.info("subject:{},dto:{}","查询审核过程（最新）", JsonUtil.toJSON(dto));
        return creditApplyService.findApprovalCourse(dto);
    }

    @ApiOperation("查询授信申请详情（最新）")
    @PostMapping("/getDetails")
    public Result getDetails(@RequestBody QueryApplyDetailsDTO dto){
        logger.info("subject:{},dto:{}","查询审核过程（最新）", JsonUtil.toJSON(dto));
        return creditApplyService.getDetails(dto);
    }

}

