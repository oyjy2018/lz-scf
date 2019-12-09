package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/8/28 15:16
 */
@Data
public class CreditApplyBaseDTO {
    @NotNull(message = "授信申请id不能为空")
    private Long creditApplyId;
}
