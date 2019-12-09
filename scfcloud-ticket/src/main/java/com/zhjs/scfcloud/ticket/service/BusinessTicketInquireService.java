package com.zhjs.scfcloud.ticket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.businessTicket.*;
import com.zhjs.scfcloud.model.entity.BusinessTicketInquire;

/**
 * 询价 相关
 * @since
 */

public interface BusinessTicketInquireService extends IService<BusinessTicketInquire> {


    Result inquire(BusinessTicketInquireAddDTO dto);

    Result inquireList(BusinessTicketInquireListDTO businessTicketInquireListDTO);

    Result cancelInquire(BusinessTicketInquireCancelDTO dto);

    Result inquireDetail(Long id);

    Result myAssignInquireList(BusinessTicketInquireMyAssignListDTO dto);

    Result allInquire(BusinessTicketAllInquireDTO dto);
}
