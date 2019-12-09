package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.credit.AddCreditUseRecordDTO;
import com.zhjs.scfcloud.model.entity.CreditTicket;
import com.zhjs.scfcloud.model.entity.CreditTicketApply;
import com.zhjs.scfcloud.model.vo.credit.CreditUseApplyDetailsVO;
import org.mapstruct.Mapper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 18:16
 *
 * @author liuchanghai
 * @create 2019-06-27 18:16
 * @since
 */
@Mapper(componentModel = "spring")
public interface CreditUseTransfer {

    CreditTicket creditUseRecordDTO2CreditTicket(AddCreditUseRecordDTO dto);

    CreditUseApplyDetailsVO po2CreditUseApplyDetailsVO(CreditTicketApply creditTicketApply);
}
