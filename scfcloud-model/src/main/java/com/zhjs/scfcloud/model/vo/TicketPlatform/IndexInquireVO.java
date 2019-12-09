package com.zhjs.scfcloud.model.vo.TicketPlatform;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页询价VO
 * @author weijie.chen
 */
@Data
public class IndexInquireVO {
    /**
     *
     * 询价单ID
     */
    private Long inquireId;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 票面金额
     * bill_amt
     */
    private BigDecimal billAmt;

    /**
     * 到期日
     * maturity_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;

}
