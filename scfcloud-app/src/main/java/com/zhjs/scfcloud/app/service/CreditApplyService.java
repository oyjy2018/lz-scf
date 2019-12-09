package com.zhjs.scfcloud.app.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.*;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 授信申请处理类
 * @author: dailongting
 * @date:2019/6/13 10:39
 */
public interface CreditApplyService {


    String saveDraft(CreditApplyDraftSaveDTO creditApplyDraftSaveDTO);

    String commit(CreditApplyCommitDTO creditApplyCommitDTO);

    String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    Result<Object> getDetail(CreditApplyQueryDTO dto);

    /**
     * 准备资料-上传附件
     * @param dto
     * @return
     */
    Result getWorkFlowFileCfg(CreditApplyQueryDTO dto);

    /**
     * app授信审批提交
     * @param dto
     * @return
     */
    Result appAuditCommit(@RequestBody com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO dto);
}
