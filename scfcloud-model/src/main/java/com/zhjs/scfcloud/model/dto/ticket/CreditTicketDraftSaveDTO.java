package com.zhjs.scfcloud.model.dto.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/6/28 17:50
 */
@Data
public class CreditTicketDraftSaveDTO {
    //公司ID
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    private Long userId;

//    //业务类型id
//    @NotNull(message = "业务类型id不能为空")
//    private Long businessTypeId;

    //业务id
    private Long businessId;

    /**
     * 当前流程code
     */
    private String flowCode;

    /**
     * 字段主键ID
     */
    private Long primaryId;

    //字段名
    @NotNull(message = "字段名不能为空")
    private String columnName;

    //字段值
    private String value;

    //第几个（针对于重复字段）
    private Integer index;
}
