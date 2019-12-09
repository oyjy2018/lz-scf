package com.zhjs.scfcloud.model.dto.businessTicket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发布询价（商票）参数
 */
@Data
public class BusinessTicketInquireAddDTO {


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
     * 票据号码
     * bill_no
     */
    @NotNull(message = "票据号码为空")
    @Size(max = 30,min = 30,message = "票据号码必须为30位")
    private String billNo;

    /**
     * 票面金额
     * bill_amt
     */
    @NotNull(message = "票面金额为空")
    @Pattern(regexp = "^\\d+(\\.\\d+)?$",message = "票据金额格式不正确")
    @DecimalMax(value = "1000000000",inclusive = false,message = "票面金额必须小于十亿元")
    @DecimalMin(value = "0",inclusive = false,message = "票面金额必须大于0元")
    private String billAmt;

    /**
     * 出票日
     * issue_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 到期日
     * maturity_date
     */
    @NotNull(message = "到期日为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    @NotNull(message = "承兑人名称为空")
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
    @NotNull(message = "剩余天数为空")
    private Integer surplusValidDays;

    /**
     * 发布截止日期
     * expiration_date
     */
    @NotNull(message = "发布截止日期为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationDate;

    /**
     * 背书次数
     * endorsed_count
     */
    @NotNull(message = "背书次数为空")
    private Integer endorsedCount;

    /**
     * 票据瑕疵
     * flaws
     */
    @NotNull(message = "票据瑕疵为空")
    private String flaws;


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
    @NotNull(message = "是否同意签署《票据收益权转让协议》为空")
    private Integer hasSignProtocols;

    //指定买家公司id
    private Long assignBuyerCompanyId;

    //指定买家公司名称
    private String assignBuyerCompanyName;

    //文件列表
    List<BusinessTicketFile> fileList;

}