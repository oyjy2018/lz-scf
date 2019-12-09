package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 电签发验证码DTO
 */
@Data
public class EsignSendCodeDTO {
    @NotNull(message = "签署类型不能为空")
    public String signParty;//签署方(a1:甲方个人,a2:甲方企业,b1:乙方个人,b2:乙方企业)

    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
