package com.zhjs.scfcloud.model.dto.credit;

import com.zhjs.scfcloud.model.dto.business.BusinessBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/6/18 18:55
 */
@Data
@Accessors(chain = true)
public class CreditApplyQueryDTO extends BusinessBaseDTO {
    @NotNull(message = "申请id不能为空")
    private Long creditApplyId;

    @ApiModelProperty("当前流程CODE")
    @NotNull(message = "流程code不能为空")
    private String flowCode;
}
