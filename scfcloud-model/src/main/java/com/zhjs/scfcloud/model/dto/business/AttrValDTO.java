package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务字段属性值 新建 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 17:03
 *
 * @author liuchanghai
 * @create 2019-06-12 17:03
 * @since
 */

@Data
public class AttrValDTO {

    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("业务属性ID")
    private Long businessAttrId;

    @ApiModelProperty("值key")
    private String valueKey;

    @ApiModelProperty("值中文")
    private String valueCh;

    @ApiModelProperty("上级属性值key")
    private String superiorAttrKey;
}
