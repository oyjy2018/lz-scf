package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.QuotationOwnerQueryDTO;
import com.zhjs.scfcloud.model.dto.QuotationTicketQueryDTO;
import com.zhjs.scfcloud.model.dto.ticket.AllQuotationListDTO;
import com.zhjs.scfcloud.model.vo.QuotationDetailVO;
import com.zhjs.scfcloud.model.vo.QuotationOwnerVO;
import com.zhjs.scfcloud.model.vo.QuotationTicketVO;
import com.zhjs.scfcloud.model.vo.businessTicket.AllQuotationTicketVO;

/**
 * 报价服务
 */
public interface QuotationService {
    /**
     * 票方-报价单列表
     * @param dto
     * @return
     */
    IPage<QuotationTicketVO> selectQuotationTicketList(QuotationTicketQueryDTO dto);

    /**
     * 报价单详情
     * @param quotationId
     * @return
     */
    QuotationDetailVO selectQuotationDetail(Long quotationId);

    /**
     * 票方-我的报价单列表
     * @param dto
     * @return
     */
    IPage<QuotationOwnerVO> selectQuotationOwnerList(QuotationOwnerQueryDTO dto);

    /**
     * 撤销报价
     * @param quotationId
     * @return
     */
    Result deleteQuotationByOwner(Long quotationId,Long userId);

    /**
     * 所有报价单列表
     * @param allQuotationListDTO
     * @param userId
     * @return
     */
    IPage<AllQuotationTicketVO> findAllQuotations(AllQuotationListDTO allQuotationListDTO,Long userId);

}
