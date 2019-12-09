package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author:weijie.chen
 * @date:2019-07-15 19:06
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_quotation")
public class BusinessTicketQuotation {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 公司名称
     * company_name
     */
    private String companyName;

    /**
     * 询价单ID
     * inquire_id
     */
    private Long inquireId;

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
     * 成交价格
     * amount
     */
    private BigDecimal amount;

    /**
     * 报价时剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;

    /**
     * 是否同意签署《票据收益权转让协议》(1:是；0：否）
     * has_sign_protocols
     */
    private Integer hasSignProtocols;

    /**
     * 报价人ID
     * quotation_person_id
     */
    private Long quotationPersonId;

    /**
     * 报价人
     * quotation_person
     */
    private String quotationPerson;

    /**
     * 接受报价时间
     * accepted_time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptedTime;

    /**
     * 报价状态(1:待接受报价;2:接受报价;3:不接受报价;4:已撤回)
     * status
     */
    private Integer status;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;
}