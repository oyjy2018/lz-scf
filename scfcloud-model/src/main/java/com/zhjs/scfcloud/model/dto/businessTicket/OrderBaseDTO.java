package com.zhjs.scfcloud.model.dto.businessTicket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/7/18 10:57
 */
@Data
public class OrderBaseDTO {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    private Long userId;

    private String userName;
}
