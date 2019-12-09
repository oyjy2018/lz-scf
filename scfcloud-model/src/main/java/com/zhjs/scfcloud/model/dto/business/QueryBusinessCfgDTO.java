package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/9/10 14:46
 */
@Data
@Accessors(chain = true)
public class QueryBusinessCfgDTO {
    @ApiModelProperty("公司ID")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("业务类型ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

    @ApiModelProperty("业务类型名称")
    private String businessType;
}
