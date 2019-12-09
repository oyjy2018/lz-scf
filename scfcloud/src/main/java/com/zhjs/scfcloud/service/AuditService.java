package com.zhjs.scfcloud.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;

/**
 * 业务流程审核相关
 */
public interface AuditService {
    /**
     * 通用参数校验方法
     */
    Result validParam(CommonAuditCommitDTO commonAuditCommitDTO);

}
