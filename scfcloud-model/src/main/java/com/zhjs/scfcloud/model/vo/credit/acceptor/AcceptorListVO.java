package com.zhjs.scfcloud.model.vo.credit.acceptor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AcceptorListVO {
    private Long id;

    private String acceptorName;

    private BigDecimal limitMoney;

    private BigDecimal useLimitMoney;

    private BigDecimal remainLimitMoney;

    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    public BigDecimal getRemainLimitMoney() {
        BigDecimal remain = limitMoney.subtract(getUseLimitMoney());
        return remain.compareTo(new BigDecimal(0)) >= 0 ? remain : new BigDecimal(0);
    }

    public BigDecimal getUseLimitMoney() {
        if (useLimitMoney == null) return new BigDecimal(0);
        if (new BigDecimal(0).compareTo(useLimitMoney) == 0) return new BigDecimal(0);
        return useLimitMoney;
    }
}
