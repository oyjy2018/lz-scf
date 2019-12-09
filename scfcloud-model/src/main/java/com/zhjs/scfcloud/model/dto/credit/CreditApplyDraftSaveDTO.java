package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  授信业务保存草稿dto
 */
@Data
public class CreditApplyDraftSaveDTO {

    //公司ID
    private Long companyId;

    private Long userId;

    private String userName;

//    //业务类型id
//    @NotNull(message = "业务类型id不能为空")
//    private Long businessTypeId;

    //业务id
    private Long businessId;

    //字段名
    @NotNull(message = "字段名不能为空")
    private String tableName;

    //字段值
    private Object value;

    //第几个（针对于重复字段）
    private Integer index;

}
