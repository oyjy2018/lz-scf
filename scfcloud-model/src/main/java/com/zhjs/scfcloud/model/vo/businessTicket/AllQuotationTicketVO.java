package com.zhjs.scfcloud.model.vo.businessTicket;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 票方-报价单VO
 */
@Data
public class AllQuotationTicketVO {
    /**
     *
     * 报价单id
     */
    private Long quotationId;

    /**
     *
     * 询价单id
     */
    private Long inquireId;

    /**
     * 报价公司名称
     * company_name
     */
    private String quotationCompanyName;

    /**
     * 报价状态(1:待接受报价;2:接受报价;3:不接受报价;4:已撤回)
     * status
     */
    private Integer quotationStatus;


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
     * 报价
     * amount
     */
    private BigDecimal amount;

    /**
     * 到期日
     * maturity_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;

    /**
     * 剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;

    /**
     * 年化率
     * annual_rate
     */
    private BigDecimal annualRate;

    /**
     * 每十万扣款
     * discount_fee
     */
    private BigDecimal discountFee;

    /**
     * 票据瑕疵
     * flaws
     */
    private String flaws;

    /**
     *  报价时间
     * create_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quotationCreateTime;
}
