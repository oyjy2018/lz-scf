package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 电签入参DTO
 */
@Data
public class EsignContractSignDTO {
    @NotNull(message = "订单id不能为空")
    public Long orderId; //订单id

    @NotNull(message = "签署方不能为空")
    public String signParty;//签署方(a1:甲方个人,a2:甲方企业,b1:乙方个人,b2:乙方企业)

    @NotNull(message = "验证码不能为空")
    @Size(max = 6,min = 6,message = "验证码必须是6位")
    public String code; // 验证码

    public String keyWord;//关键字
}
