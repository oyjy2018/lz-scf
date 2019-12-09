package com.zhjs.scfcloud.system.service;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.credit.*;

/**
 * 用信管理业务逻辑接口
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 11:17
 *
 * @author liuchanghai
 * @create 2019-06-27 11:17
 * @since
 */
public interface CreditUseService {

    /**
     * 查询用信我的申请列表
     * @param dto 入参
     * @return
     */
    Result findCreditUseApplyMyList(CreditUseApplyMyListQueryDTO dto);

    /**
     * 查询用信所有申请列表
     * @param dto 入参
     * @return
     */
    Result findCreditUseApplyAllList(CreditUseApplyAllListQueryDTO dto);

    /**
     * 查询用信审批列表
     * @param dto 入参
     * @return
     */
    Result findCreditUseAuditList(CreditUseAuditListQueryDTO dto);

    /**
     * 录入用信记录
     * @param dto 入参
     * @return
     */
    Result addCreditUseRecord(AddCreditUseRecordDTO dto);

    /**
     * 查看用信申请详情
     * @param dto 入参
     * @return
     */
    Result findCreditUseApplyDetails(BaseIdDTO dto);

    /**
     * 查看用信申请详情
     * @param dto 入参
     * @return
     */
    Result findCreditUseApplyDocuemnt(BaseIdDTO dto);

    /**
     * App端-查看用信申请详情
     * @param dto 入参
     * @return
     */
    Result findAppCreditUseApplyDetails(BaseIdDTO dto);

    String myAppList(CreditUseMyListAppDTO creditUseMyListAppDTO);

    String myWebList(CreditUseMyListWebDTO dto);

    String allWebList(CreditUseAllListWebDTO dto);

    String updateRefundStatus(CreditUseUpdateRefundStatusDTO dto);

    String webDetail(CreditUseDetailWebDTO dto);

    String ticketList(CreditTicketListWebDTO dto);

    String ticketDetail(BaseIdDTO dto);

    Result updateTicketStatus(BaseSingleUpdateDTO dto);
}
