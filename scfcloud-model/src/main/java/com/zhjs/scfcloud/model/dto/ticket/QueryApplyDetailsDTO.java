package com.zhjs.scfcloud.model.dto.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/8/29 16:18
 */
@Data
public class QueryApplyDetailsDTO extends CreditTicketApplyBaseDTO {
    @NotNull(message = "查询类型不能为空")
    private String queryType;
}
