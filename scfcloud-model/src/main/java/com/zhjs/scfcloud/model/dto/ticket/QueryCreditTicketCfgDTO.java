package com.zhjs.scfcloud.model.dto.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查询授信开票
 * @author: dailongting
 * @date:2019/6/27 17:47
 */
@Data
public class QueryCreditTicketCfgDTO {
    @ApiModelProperty("授信记录ID")
    private Long creditRecordId;

//    @ApiModelProperty("业务类型ID")
//    @NotNull(message = "业务类型ID不能为空")
//    private Long businessTypeId;

    @ApiModelProperty("开票申请业务ID")
    private Long creditTicketApplyId;

    @ApiModelProperty("流程code")
    @NotNull(message = "流程code不能为空")
    private String flowCode;

    /**
     * create:创建页面；view:查看页面；edit:编辑页面；audit:审批页面； PS：枚举为QueryType
     */
    @ApiModelProperty("查询类型")
    @NotNull(message = "查询类型不能为空")
    private String queryType;
}
