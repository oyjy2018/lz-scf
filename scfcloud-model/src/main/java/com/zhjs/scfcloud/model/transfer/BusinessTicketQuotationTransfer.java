package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketQuoteAddDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketQuotation;
import org.mapstruct.Mapper;

/**
 * 报价价相关 转换
 */

@Mapper(componentModel = "spring")
public interface BusinessTicketQuotationTransfer {

    //报价dto转为报价do
    BusinessTicketQuotation toBusinessTicketQuotation(BusinessTicketQuoteAddDTO dto);
}
