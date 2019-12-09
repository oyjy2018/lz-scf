package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompanySystemVersionNameVO {
    @ApiModelProperty("风控平台系统名称")
    private String riskSystemName;
    @ApiModelProperty("风控平台版本名称")
    private String riskSystemVersionName;
    @ApiModelProperty("风控平台系统名称")
    private String ticketSystemName;
    @ApiModelProperty("票据交易平台版本名称")
    private String ticketSystemVersionName;
}
