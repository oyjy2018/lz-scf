package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加行业信息 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 15:30
 *
 * @author liuchanghai
 * @create 2019-05-23 15:30
 * @since
 */

@Data
public class AddNatureDTO {

    @ApiModelProperty("键")
    private String dictKey;

    @ApiModelProperty("值")
    private String dictValue;

    @ApiModelProperty("上级键")
    private String parentKey;

    @ApiModelProperty("备注一")
    private String remark1;

    @ApiModelProperty("备注一")
    private String remark2;

    @ApiModelProperty("备注一")
    private String remark3;
}
