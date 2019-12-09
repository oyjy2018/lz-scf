package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.OperateLogDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 授信申请
 */

@FeignClient(value = "scfcloud-system", contextId = "creditApply")
public interface CreditApplyFeignService {

    //授信申请保存草稿
    @PostMapping("/creditApply/saveDraft")
    String saveDraft(@RequestBody CreditApplyDraftSaveDTO creditApplyDraftSaveDTO);

    //授信申请提交
    @PostMapping("/creditApply/commit")
    String commit(@RequestBody CreditApplyCommitDTO creditApplyCommitDTO);

    //授信申请保存文件
    @PostMapping("/creditApply/saveFile")
    String saveFile(@RequestBody CreditApplyFileSaveDTO creditApplyFileSaveDTO);

    //授信申请web端我的申请列表
    @PostMapping("/creditApply/myApplyList")
    String myApplyList(@RequestBody CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    @ApiOperation("授信审批提交")
    @PostMapping("/creditApply/auditCommit")
    Result<User> auditCommit(@RequestBody CommonAuditCommitDTO dto);

    //授信申请所有申请列表
    @PostMapping("/creditApply/allApplyList")
    String allApplyList(@RequestBody CreditApplyAllListQueryDTO creditApplyAllListQueryDTO);

    @ApiOperation("查询授信申请详情")
    @PostMapping("/creditApply/findCreditApplyDetails")
    Result findCreditApplyDetails(@RequestBody CreditApplyQueryDTO dto);

    @ApiOperation("查询授信申请文件资料")
    @PostMapping("/creditApply/document")
    Result documents(@RequestBody CreditApplyQueryDTO dto);

    @ApiOperation("审核日志")
    @PostMapping("/creditApply/findAuditLogList")
    String findAuditLogList(@RequestBody AuditLogListDTO dto);

    @ApiOperation("授信审批列表")
    @PostMapping("/creditApply/auditList")
    String auditList(@RequestBody CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO);

    @ApiOperation("操作日志")
    @PostMapping("/creditApply/findOperateLogList")
    String findOperateLogList(@RequestBody OperateLogDTO dto);

    @PostMapping("/creditApply/deleteCreditItem")
    String deleteCreditItem(@RequestBody CreditItemDeleteDTO creditItemDeleteDTO);

    @ApiOperation("查询审核过程（最新）")
    @PostMapping("/creditApply/findApprovalCourse")
    Result findApprovalCourse(@RequestBody CreditApplyBaseDTO dto);

    @ApiOperation("查询授信申请详情（最新）")
    @PostMapping("/creditApply/getDetails")
    Result getDetails(@RequestBody QueryApplyDetailsDTO dto);
}
