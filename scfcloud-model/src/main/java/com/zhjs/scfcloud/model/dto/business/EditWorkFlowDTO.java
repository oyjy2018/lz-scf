package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/9/19 13:55
 */
@Data
@Accessors(chain = true)
public class EditWorkFlowDTO extends BusinessTypeBaseDTO{

    @ApiModelProperty("流程扭转配置集合")
    @NotNull(message = "流程扭转配置集合不能为空")
    private List<AddWorkFlowDTO> addWorkFlowList;

    private Long userId;

    private String userName;
}
