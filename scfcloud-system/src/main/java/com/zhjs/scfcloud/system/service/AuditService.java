package com.zhjs.scfcloud.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.entity.CreditApplyAudit;

/**
 * 审批相关
 */
public interface AuditService{

    Result commonAuditCommit(CommonAuditCommitDTO commonAuditCommitDTO);


}
