package com.zhjs.scfcloud.model.dto.ticket;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PermissionBankListDTO {

    //公司ID
    private String companyName;

    //账户类型
    private String accountType;

    //账户状态
    private Integer accountStatus;
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
