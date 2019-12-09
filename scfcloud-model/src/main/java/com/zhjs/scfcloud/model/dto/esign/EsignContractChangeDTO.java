package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 合同内容DTO
 */
@Data
public class EsignContractChangeDTO {
    @NotNull(message = "订单id不能为空")
    public Long orderId; //订单id

    @NotNull(message = "合同类型不能为空")
    public String contractType;//合同类型

    public String fileUrl; //文件地址

    public Long userId;
}
