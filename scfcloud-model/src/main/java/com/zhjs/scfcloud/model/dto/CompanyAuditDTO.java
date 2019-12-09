package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompanyAuditDTO{

    @ApiModelProperty("公司类别")
    private String companyCategory;

    @ApiModelProperty("风控平台,版本Id")
    private Long riskControlSystemVersionId;

    @ApiModelProperty("商票交易平台,版本Id")
    private Long ticketSystemVersionId;

    @ApiModelProperty("可使用的业务流程")
    private List<String> businessFlowList;

    @ApiModelProperty("审核结果(1:通过 2:拒绝)")
    @NotNull(message = "审核结果不能为空")
    private Integer status;

}
