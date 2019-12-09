package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreditItemDeleteDTO {
    //
    @NotNull(message = "申请id不能为空")
    private Long creditApplyId;

    @NotNull(message = "项目序号不能为空")
    private Integer itemIndex;
}
