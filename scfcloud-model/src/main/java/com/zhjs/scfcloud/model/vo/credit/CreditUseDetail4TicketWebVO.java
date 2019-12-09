package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.model.enums.CreditUseEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 用信详情(类型为开商票)vo web
 */

@Data
public class CreditUseDetail4TicketWebVO extends CreditTicketDetailWebVO {

    //用信类型
    private Integer useType;

    //用信类型名（0：开商票，1：工程贷）
    private String useTypeName;

    public String getUseTypeName() {
        return CreditUseEnum.UseType.getUseTypeName(this.useType);
    }
}
