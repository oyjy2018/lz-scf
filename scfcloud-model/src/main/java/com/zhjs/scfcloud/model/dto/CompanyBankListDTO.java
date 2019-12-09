package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompanyBankListDTO {
    //公司ID
    private Long companyId;

    //账户类型
    private String accountType;
    /**
     * 分页数
     */
    @NotNull(message = "分页数不能为空")
    private Long current;

    /**
     * 分页大小
     */
    @NotNull(message = "分页大小不能为空")
    private Long size;

}
