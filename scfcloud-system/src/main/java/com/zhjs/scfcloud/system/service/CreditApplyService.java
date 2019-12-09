package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.AuditLogListDTO;
import com.zhjs.scfcloud.model.dto.CommonAuditCommitDTO;
import com.zhjs.scfcloud.model.dto.credit.*;
import com.zhjs.scfcloud.model.entity.User;

/**
 * 授信申请的业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 15:46
 *
 * @author liuchanghai
 * @create 2019-06-11 15:46
 * @since
 */
public interface CreditApplyService {

    //授信申请保存文件
    String saveFile(CreditApplyFileSaveDTO creditApplyFileSaveDTO);

    //授信申请保存草稿
    String saveDraft(CreditApplyDraftSaveDTO creditApplyDraftSaveDTO);

    //授信申请提交
    String commit(CreditApplyCommitDTO creditApplyCommitDTO);

    String myApplyList(CreditApplyMyListQueryDTO creditApplyMyListQueryDTO);

    /**
     * 根据配置字段查询授信业务详情
     * @param dto
     * @return
     */
    Result findCreditApplyDetails(CreditApplyQueryDTO dto);

    /**
     * 查询授信-文件资料
     * @param dto
     * @return
     */
    Result findCreditApplyFile(CreditApplyQueryDTO dto);

    /**
     * 授信审核提交
     * @param dto 入参
     * @return
     */
    Result<User> auditCommit(CommonAuditCommitDTO dto);

    /**
     * 查询授信审核日记
     * @param auditLogListDTO 入参 授信申请ID
     * @return
     */
    String findAuditLogList(AuditLogListDTO auditLogListDTO);

    String allApplyList(CreditApplyAllListQueryDTO creditApplyAllListQueryDTO);

    String auditList(CreditApplyAuditListQueryDTO creditApplyAuditListQueryDTO);

    /**
     * app授信审核提交
     * @param dto
     * @return
     */
    Result appAuditCommit(com.zhjs.scfcloud.model.dto.credit.app.CreditAuditCommitDTO dto);

    String deleteCreditItem(CreditItemDeleteDTO creditItemDeleteDTO);

    /**
     * 查询审核过程（最新）
     * @param dto
     * @return
     */
    Result findApprovalCourse(CreditApplyBaseDTO dto);

    /**
     * 查询申请授信详情（新）
     * @param dto
     * @return
     */
    Result getDetails(QueryApplyDetailsDTO dto);
}
