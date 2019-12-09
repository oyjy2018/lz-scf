package com.zhjs.scfcloud.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAllListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyAuditFinishListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseApplyMyListQueryDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseAuditListQueryDTO;
import com.zhjs.scfcloud.model.entity.CreditTicketApply;
import com.zhjs.scfcloud.model.vo.credit.CreditUseApplyAuditFinishListVO;
import com.zhjs.scfcloud.model.vo.credit.CreditUseApplyListWebVO;

import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 * @author:dailongting
 * @date:2019-08-07 10:57
 */
public interface CreditTicketApplyMapper extends BaseMapper<CreditTicketApply> {
    //我的申请
    List<CreditUseApplyListWebVO> findCreditUseApplyMyList(CreditUseApplyMyListQueryDTO dto);

    //所有申请
    List<CreditUseApplyListWebVO> findCreditUseApplyAllList(CreditUseApplyAllListQueryDTO dto);
    /**
     * 查询开票申请详情
     * @param creditTicketApplyId
     * @return
     */
    Map<String, Object> findCreditTicketApplyDetails(Long creditTicketApplyId);

    List<CreditUseApplyListWebVO> getAuditList(CreditUseAuditListQueryDTO dto);

    //审批完成列表
    List<CreditUseApplyAuditFinishListVO> getAuditFinishList(CreditUseApplyAuditFinishListQueryDTO dto);
}