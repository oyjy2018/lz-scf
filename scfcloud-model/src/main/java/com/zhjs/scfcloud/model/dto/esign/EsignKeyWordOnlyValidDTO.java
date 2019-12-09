package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 合同关键字校验DTO
 */
@Data
public class EsignKeyWordOnlyValidDTO {
    @NotNull(message = "订单id不能为空")
    public Long orderId; //订单id

    @NotBlank(message = "关键字不能为空")
    @Size(max = 20,message = "关键字不能超过20位")
    public String keyWord;
}
