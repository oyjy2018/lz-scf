package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  授信业务保存草稿dto
 */
@Data
public class CreditApplyCommitDTO {

    //公司ID
    private Long companyId;

    //员工id
    private Long userId;

    //员工姓名
    private String userName;

//    //业务类型id
//    @NotNull(message = "业务类型id不能为空")
//    private Long businessTypeId;

    //业务id
    @NotNull(message = "业务id不能为空")
    private Long businessId;

}
