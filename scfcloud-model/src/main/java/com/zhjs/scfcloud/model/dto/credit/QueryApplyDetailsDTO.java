package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/8/29 10:49
 */
@Data
public class QueryApplyDetailsDTO extends CreditApplyBaseDTO {
    @NotNull(message = "查询类型不能为空")
    private String queryType;
}
