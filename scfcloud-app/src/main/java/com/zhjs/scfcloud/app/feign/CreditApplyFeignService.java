package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

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

    //授信申请app端我的申请列表
    @PostMapping("/creditApply/myApplyList")
    String myApplyList(@RequestBody CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    //查询授信申请详情
    @PostMapping("/creditApply/findCreditApplyDetails")
    Result getDetail(@RequestBody CreditApplyQueryDTO dto);

    @ApiOperation("app授信审核提交")
    @PostMapping("/creditApply/appAuditCommit")
    Result appAuditCommit(@RequestBody com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO dto);
}
