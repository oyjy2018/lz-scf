package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.ticket.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/6/28 17:00
 */
@FeignClient(value = "scfcloud-system", contextId = "creditTicketApply")
public interface CreditTicketApplyFeignService {

    /**
     * 查询开票申请详情
     * @param creditTicketApplyId
     * @return
     */
    @PostMapping("/creditTicketApply/findCreditTicketApplyDetails")
    Result<Map<String, Object>> findCreditTicketApplyDetails(@RequestParam("creditTicketApplyId") Long creditTicketApplyId);

    /**
     * 保存开票申请字段值
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/saveDraft")
    Result saveDraft(@RequestBody CreditTicketDraftSaveDTO dto);

    //开商票审批
    @PostMapping("/creditTicketApply/auditCommit")
    Result auditCommit(@RequestBody CommonAuditCommitDTO dto);

    /**
     * 开票申请保存文件（需判断是否需要新增开票申请草稿记录）
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/saveFile")
    Result saveFile(@RequestBody CreditTicketFileSaveDTO dto);

    /**
     * 提交开票申请
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/applyCommit")
    Result applyCommit(@RequestBody CreditTicketApplyCommitDTO dto);

    /**
     * 查询审核过程数据
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/findApprovalCourse")
    Result findApprovalCourse(@RequestBody CreditTicketApplyBaseDTO dto);

    /**
     * 查询开票申请详情（新）
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/getDetails")
    Result getDetails(@RequestBody QueryApplyDetailsDTO dto);

    /**
     * 审批完成列表
     * @param dto
     * @return
     */
    @PostMapping("/creditTicketApply/getAuditFinishList")
    Result getAuditFinishList(@RequestBody CreditUseApplyAuditFinishListQueryDTO dto);
}
