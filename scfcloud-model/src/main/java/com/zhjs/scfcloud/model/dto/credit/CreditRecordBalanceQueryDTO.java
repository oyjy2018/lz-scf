package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreditRecordBalanceQueryDTO {
    //调用方id
    @NotBlank(message = "系统id为空")
    private String partnerId;
    //工程云项目id
    @NotBlank(message = "项目id为空")
    private String projectId;

    @NotBlank(message = "项目关联商务经理项目为空")
    private String managerName;
}
