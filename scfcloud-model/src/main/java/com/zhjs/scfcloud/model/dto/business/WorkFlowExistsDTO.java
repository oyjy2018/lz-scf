package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/9/19 16:38
 */
@Data
@Accessors(chain = true)
public class WorkFlowExistsDTO extends BusinessTypeBaseDTO{
    @ApiModelProperty("扭转前流程CODE")
    @NotNull(message = "扭转前流程CODE不能为空")
    private String beforeFlow;

    @ApiModelProperty("扭转后流程CODE")
    @NotNull(message = "扭转后流程CODE不能为空")
    private String afterFlow;
}
