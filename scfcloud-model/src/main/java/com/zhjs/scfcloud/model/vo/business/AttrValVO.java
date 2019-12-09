package com.zhjs.scfcloud.model.vo.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务字段属性值 视图数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-12 14:28
 *
 * @author liuchanghai
 * @create 2019-06-12 14:28
 * @since
 */

@Data
public class AttrValVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("值key")
    private String valueKey;

    @ApiModelProperty("值中文")
    private String valueCh;

    @ApiModelProperty("上级属性值key")
    private String superiorAttrKey;
}
