package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加银行卡信息 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 17:47
 *
 * @author liuchanghai
 * @create 2019-05-23 17:47
 * @since
 */

@Data
public class AddBankAccountDTO {

    @ApiModelProperty("账号类型:0-开户行 1-商票行")
    private String accountType;

    @ApiModelProperty("银行账号")
    private String accountNo;

    @ApiModelProperty("账号名称")
    private String accountName;

    @ApiModelProperty("银行")
    private Long bankId;

    @ApiModelProperty("支行")
    private String bankBranch;
}
