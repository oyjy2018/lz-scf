package com.zhjs.scfcloud.model.dto.businessTicket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 撤销发布参数
 */
@Data
public class BusinessTicketInquireCancelDTO {

   //询价单id
   @NotNull(message = "询价id为空")
   private Long id;

   //操作人id
   private Long userId;
}