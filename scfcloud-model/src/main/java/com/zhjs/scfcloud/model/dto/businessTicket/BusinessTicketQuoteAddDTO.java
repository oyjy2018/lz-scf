package com.zhjs.scfcloud.model.dto.businessTicket;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BusinessTicketQuoteAddDTO {

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
    @NotNull(message = "询价单ID为空")
    private Long inquireId;

    /**
     * 年化率
     * annual_rate
     */
    @NotNull(message = "年化率为空")
    private BigDecimal annualRate;

    /**
     * 每十万扣款
     * discount_fee
     */
    @NotNull(message = "每十万扣款为空")
    private BigDecimal discountFee;

    /**
     * 成交价格
     * amount
     */
    @NotNull(message = "成交价格为空")
    private BigDecimal amount;

    /**
     * 报价时剩余天数
     * surplus_valid_days
     */
    @NotNull(message = "报价时剩余天数为空")
    private Integer surplusValidDays;

    /**
     * 是否同意签署《票据收益权转让协议》(1:是；0：否）
     * has_sign_protocols
     */
    @NotNull(message = "是否同意签署《票据收益权转让协议》为空")
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

}
