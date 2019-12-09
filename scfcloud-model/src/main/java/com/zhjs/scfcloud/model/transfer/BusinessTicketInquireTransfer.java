package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.businessTicket.BusinessTicketInquireAddDTO;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;
import org.mapstruct.Mapper;

/**
 * 询价相关 转换
 */

@Mapper(componentModel = "spring")
public interface BusinessTicketInquireTransfer {

    //询价发布dto转为询价do
    BusinessTicketInquire toBusinessTicketInquire(BusinessTicketInquireAddDTO dto);
}
