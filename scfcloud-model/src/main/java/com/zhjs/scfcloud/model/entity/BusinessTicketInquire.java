package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author:dailongting
 * @date:2019-09-03 15:36
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_inquire")
public class BusinessTicketInquire {
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
     * 交易合同号
     * trade_contract_no
     */
    private String tradeContractNo;

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
     * 出票日
     * issue_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    /**
     * 到期日
     * maturity_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date maturityDate;

    /**
     * 出票人名称
     * drawer_name
     */
    private String drawerName;

    /**
     * 出票人开户行行号
     * drawer_bank
     */
    private String drawerBank;

    /**
     * 出票人开户行名称
     * drawer_bank_bame
     */
    private String drawerBankBame;

    /**
     * 出票人开户账号
     * drawer_account
     */
    private String drawerAccount;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 承兑人开户行名称
     * accepter_bank_name
     */
    private String accepterBankName;

    /**
     * 承兑人开户行行号
     * accepter_bank
     */
    private String accepterBank;

    /**
     * 承兑人开户账号
     * accepter_account
     */
    private String accepterAccount;

    /**
     * 收款人名称(收款人指收票人)
     * payee_name
     */
    private String payeeName;

    /**
     * 收款人开户行名称
     * payee_bank_name
     */
    private String payeeBankName;

    /**
     * 收款人开户行行号
     * payee_bank
     */
    private String payeeBank;

    /**
     * 收款人开户账号
     * payee_account
     */
    private String payeeAccount;

    /**
     * 剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;

    /**
     * 发布截止日期
     * expiration_date
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

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
     * 票据状态（0：未背书；1：背书代签收；2：背书已签收）
     * bill_status
     */
    private Integer billStatus;

    /**
     * 发布人ID
     * publish_person_id
     */
    private Long publishPersonId;

    /**
     * 发布人
     * publish_person
     */
    private String publishPerson;

    /**
     * 是否同意签署《票据收益权转让协议》(1:是；0：否）
     * has_sign_protocols
     */
    private Integer hasSignProtocols;

    /**
     * 指定买家公司id
     * assign_buyer_company_id
     */
    private Long assignBuyerCompanyId;

    /**
     * 指定买家公司名称
     * assign_buyer_company_name
     */
    private String assignBuyerCompanyName;

    /**
     * 询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}