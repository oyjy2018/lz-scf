package com.zhjs.scfcloud.system.controller;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import com.zhjs.scfcloud.system.service.CreditTicketApplyService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 开票申请相关
 * @author: dailongting
 * @date:2019/6/28 16:47
 */
@Api(tags = "开票申请相关")
@RestController
@RequestMapping("/creditTicketApply")
public class CreditTicketApplyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CreditTicketApplyService creditTicketApplyService;

    /**
     * 查询开票申请详情
     * @param creditTicketApplyId
     * @return
     */
    @PostMapping("/findCreditTicketApplyDetails")
    public Result<Map<String, Object>> findCreditTicketApplyDetails(@RequestParam("creditTicketApplyId") Long creditTicketApplyId){
        logger.info("查询开票申请详情：{}", JsonUtil.toJSON(creditTicketApplyId));
        Map<String, Object> details = creditTicketApplyService.findCreditTicketApplyDetails(creditTicketApplyId);
        if(details == null) {
            return Result.failure("开票申请详情数据丢失");
        }
        return Result.success(details);
    }

    /**
     * 保存开票申请字段值
     * @param dto
     * @return
     */
    @PostMapping("/saveDraft")
    public Result saveDraft(@RequestBody CreditTicketDraftSaveDTO dto){
        logger.info("保存开票申请字段值：{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.saveDraft(dto);
    }

    //开商票审批
    @PostMapping("/auditCommit")
    public Result auditCommit(@RequestBody CommonAuditCommitDTO dto){
        logger.info("开商票审批：{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.auditCommit(dto);
    }

    /**
     * 开票申请保存文件（需判断是否需要新增开票申请草稿记录）
     * @param dto
     * @return
     */
    @PostMapping("/saveFile")
    public Result saveFile(@RequestBody CreditTicketFileSaveDTO dto){
        logger.info("开票申请保存文件（需判断是否需要新增开票申请草稿记录）:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.saveFile(dto);
    }

    /**
     * 提交开票申请
     * @param dto
     * @return
     */
    @PostMapping("/applyCommit")
    public Result applyCommit(@RequestBody CreditTicketApplyCommitDTO dto){
        logger.info("提交开票申请:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.applyCommit(dto);
    }

    /**
     * 查询审核过程数据
     * @param dto
     * @return
     */
    @PostMapping("/findApprovalCourse")
    public Result findApprovalCourse(@RequestBody CreditTicketApplyBaseDTO dto){
        logger.info("查询审核过程数据:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.findApprovalCourse(dto);
    }

    /**
     * 查询开票申请详情（新）
     * @param dto
     * @return
     */
    @PostMapping("/getDetails")
    public Result getDetails(@RequestBody QueryApplyDetailsDTO dto){
        logger.info("查询开票申请详情（新）:{}", JsonUtil.toJSON(dto));
        return creditTicketApplyService.getDetails(dto);
    }

    @ApiOperation("审批完成列表")
    @PostMapping("/getAuditFinishList")
    public Result getAuditFinishList(@RequestBody CreditUseApplyAuditFinishListQueryDTO dto) {
        logger.info("审批完成列表:{}",JsonUtil.toSerializerJSON(dto));
        return creditTicketApplyService.getAuditFinishList(dto);
    }
}
