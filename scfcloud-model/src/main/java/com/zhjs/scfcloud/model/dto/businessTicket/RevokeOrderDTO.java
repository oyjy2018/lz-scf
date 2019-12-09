package com.zhjs.scfcloud.model.dto.businessTicket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/7/19 16:53
 */
@Data
public class RevokeOrderDTO extends OrderBaseDTO{

    /**
     * 用户类型（1：票方；2：资方）
     */
    @NotNull(message = "用户类型不能为空")
    public Integer userType;
}
