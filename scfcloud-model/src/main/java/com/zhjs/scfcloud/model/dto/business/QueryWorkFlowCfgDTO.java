package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: dailongting
 * @date:2019/6/15 11:23
 */
@Data
public class QueryWorkFlowCfgDTO extends BusinessBaseDTO{

    @ApiModelProperty("当前流程CODE")
    private String flowCode;
}
