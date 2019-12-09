package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务字段属性值 查询入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 15:53
 *
 * @author liuchanghai
 * @create 2019-06-12 15:53
 * @since
 */

@Data
public class QueryAttrValDTO {

    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("业务属性ID")
    private Long businessAttrId;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("上级属性值key")
    private String superiorAttrKey;
}
