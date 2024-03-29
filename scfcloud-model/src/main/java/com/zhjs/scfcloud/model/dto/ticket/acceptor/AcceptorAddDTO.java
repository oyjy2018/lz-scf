package com.zhjs.scfcloud.model.dto.ticket.acceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.model.annotation.IsDigit;
import lombok.Data;

import javax.crypto.Mac;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class AcceptorAddDTO extends Page {

    @NotNull(message = "承兑方名称不能为空")
    private String acceptorName;

    @NotBlank(message = "贴现额度不能为空")
    @IsDigit(message = "贴现额度必须是数字")
    @Digits(integer = 10,fraction = 2,message = "整数位不能超过10位,小数位不能超过2位")
    private String limitMoney;

    private String remark;

    private Long companyId;

    private Long userId;
}
