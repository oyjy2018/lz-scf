package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/9/18 10:47
 */
@Data
@Accessors(chain = true)
public class BusinessTypeBaseDTO {
    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("业务类型ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;
}
