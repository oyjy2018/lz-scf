package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketQuoteAddDTO;
import com.zhjs.scfcloud.model.dto.businessTicket.QuotationDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketQuotation;

/**
 * 询价 相关
 * @since
 */

public interface BusinessTicketQuotationService extends IService<BusinessTicketQuotation> {


    Result quote(BusinessTicketQuoteAddDTO dto);

    /**
     * 接受报价
     * @param dto
     * @return
     */
    Result acceptedQuotation(QuotationDTO dto);

    /**
     * 拒绝报价
     * @param dto
     * @return
     */
    Result rejectQuotation(QuotationDTO dto);
}
