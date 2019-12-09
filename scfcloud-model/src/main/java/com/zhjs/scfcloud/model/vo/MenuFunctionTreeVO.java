package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author weijie.chen
 */
@Data
public class MenuFunctionTreeVO {

    @ApiModelProperty("功能ID")
    private  Long functionId;

    @ApiModelProperty("系统ID")
    private  Long systemId;

    @ApiModelProperty("功能代码")
    private String functionCode;

    @ApiModelProperty("功能名称")
    private String functionName;
}
