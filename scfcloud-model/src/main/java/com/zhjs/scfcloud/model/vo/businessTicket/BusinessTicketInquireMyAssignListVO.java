package com.zhjs.scfcloud.model.vo.businessTicket;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 我的发布vo
 */
@Data
public class BusinessTicketInquireMyAssignListVO extends BusinessTicketInquireListVO{

    //询价方名称
    private String inquireName;

    //年化率
    private BigDecimal annualRate;

    //每十万扣款
    private String discountFee;

    public BigDecimal getAnnualRate() {
        if (annualRate != null && new BigDecimal(0).compareTo(annualRate) == 0) {
            return new BigDecimal(0);
        }
        return annualRate;
    }
}