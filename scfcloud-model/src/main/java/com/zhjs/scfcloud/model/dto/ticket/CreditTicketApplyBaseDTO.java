package com.zhjs.scfcloud.model.dto.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/8/29 15:34
 */
@Data
public class CreditTicketApplyBaseDTO {
    @NotNull(message = "业务id不能为空")
    private Long creditTicketId;
}
