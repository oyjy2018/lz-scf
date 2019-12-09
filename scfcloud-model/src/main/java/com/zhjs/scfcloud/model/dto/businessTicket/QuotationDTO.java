package com.zhjs.scfcloud.model.dto.businessTicket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/7/16 14:43
 */
@Data
public class QuotationDTO {

    @NotNull(message = "报价单ID不能为空")
    private Long quotationId;

    private Long userId;

    private String userName;

}
