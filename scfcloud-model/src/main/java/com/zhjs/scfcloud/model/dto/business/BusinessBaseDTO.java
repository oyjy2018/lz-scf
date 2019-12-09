package com.zhjs.scfcloud.model.dto.business;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 业务相关基础DTO
 * @author: dailongting
 * @date:2019/6/15 14:55
 */
@Data
@Accessors(chain = true)
public class BusinessBaseDTO {
    @ApiModelProperty("公司ID")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

//    @ApiModelProperty("业务类型ID")
//    @NotNull(message = "业务类型ID不能为空")
//    private Long businessTypeId;
//
//    @ApiModelProperty("业务类型名称")
//    private String businessType;

    /**
     * create:创建页面；view:查看页面；edit:编辑页面；audit:审批页面； PS：枚举为QueryType
     */
    @ApiModelProperty("查询类型")
    private String queryType;
}
