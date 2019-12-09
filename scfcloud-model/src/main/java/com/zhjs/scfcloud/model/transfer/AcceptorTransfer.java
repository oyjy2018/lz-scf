package com.zhjs.scfcloud.model.transfer;

import com.zhjs.scfcloud.model.dto.ticket.acceptor.AcceptorAddDTO;
import com.zhjs.scfcloud.model.entity.Acceptor;
import org.mapstruct.Mapper;

/**
 * 承兑方转换
 */

@Mapper(componentModel = "spring")
public interface AcceptorTransfer {

   Acceptor toAcceptor(AcceptorAddDTO dto);
}
