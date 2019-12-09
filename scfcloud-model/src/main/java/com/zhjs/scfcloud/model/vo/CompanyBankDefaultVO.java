package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author weijie.chen
 */
@Data
public class CompanyBankDefaultVO {

    /**
     * 账户类型
     */
    @NotNull(message = "账户类型不能为空")
    private String accountType;
}
