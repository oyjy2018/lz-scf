package com.zhjs.scfcloud.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 报价详情VO
 */
@Data
public class QuotationDetailVO {

    /**
     *
     * 询价单id
     */
    private Long inquireId;

    /**
     * 询价方公司
     */
    private String inquireCompanyName;

    /**
     * 询价方联系人
     */
    private String inquireUserName;

    /**
     * 询价方联系人电话
     */
    private String inquireUserPhone;


    /**
     * 询价方联系人邮箱
     */
    private String inquireUserEmail;


    /**
     * 报价方公司名称
     * company_name
     */
    private String quotationCompanyName;


    /**
     * 报价方联系人
     */
    private String quotationUserName;

    /**
     * 报价方联系人电话
     */
    private String quotationUserPhone;


    /**
     * 报价方联系人邮箱
     */
    private String quotationUserEmail;


    /**
     *  报价时间
     * create_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date quotationTime;
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
     * 票据号码
     * bill_no
     */
    private String billNo;
    /**
     * 票面金额
     * bill_amt
     */
    private BigDecimal billAmt;
    /**
     * 询价单到期日
     * maturity_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;
    /**
     * 报价时剩余天数
     * surplus_valid_days
     */
    private Integer quotationSurplusValidDays;

    /**
     * 背书次数
     * endorsed_count
     */
    private Integer endorsedCount;

    /**
     * 票据瑕疵
     * flaws
     */
    private String flaws;
    /**
     * 报价
     * amount
     */
    private BigDecimal amount;

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
}
