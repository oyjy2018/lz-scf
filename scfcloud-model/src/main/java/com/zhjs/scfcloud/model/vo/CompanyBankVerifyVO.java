package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author weijie.chen
 */
@Data
public class CompanyBankVerifyVO {

    /**
     * 主键ID
     * id
     */
    @NotNull(message = "公司银行主键ID不能为空")
    private Long companyBankId;

    /**
     * 打款金额，单位：元
     * company_id
     */
    @NotNull(message = "打款金额不能为空")
    private BigDecimal amount;
}
