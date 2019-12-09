package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商票详情vo web
 */

@Data
public class CreditTicketDetailWebVO {

    //用信id
    private Long creditUseId;

    //授信id
    private Long creditId;

    /**
     * 出票日期
     * ticket_start
     */
    private LocalDate ticketStart;

    /**
     * 到期日期
     * ticket_end
     */
    private LocalDate ticketEnd;

    /**
     * 票据号码
     * ticket_no
     */
    private String ticketNo;

    /**
     * 票据状态
     * ticket_status
     */
    // private Integer ticketStatus;

    /**
     * 票据金额
     * ticket_money
     */
    private BigDecimal ticketMoney;

    /**
     * 能否转让
     * is_transfer
     */
    private Integer isTransfer;

    /**
     * 交易合同号
     * trade_contract_no
     */
    private String tradeContractNo;

    /**
     * 出票人公司全称
     * ticket_give_company
     */
    private String ticketGiveCompany;

    /**
     * 收票人公司全称
     * ticket_get_company
     */
    private String ticketGetCompany;

    /**
     * 承兑人公司全称
     * acceptor_company
     */
    private String acceptorCompany;

    /**
     * 承兑人账号
     * acceptor_account
     */
    private String acceptorAccount;

    /**
     * 承兑人开户行
     * acceptor_bank
     */
    private String acceptorBank;

    /**
     * 承兑人开户行行号
     * acceptor_bank_no
     */
    private String acceptorBankNo;

    /**
     * 承兑人开户行省code
     * acceptor_bank_province_code
     */
    private String acceptorBankProvinceCode;

    /**
     * 承兑人开户行市code
     * acceptor_bank_city_code
     */
    private String acceptorBankCityCode;

    /**
     * 承兑人开户行支行
     * acceptor_bank_branch
     */
    private String acceptorBankBranch;

    //商票正面图片地址
    @JSONField(serialize = false)
    private String ticketFrontImgUrl;

    //商票反面图片地址
    @JSONField(serialize = false)
    private String ticketBackImgUrl;

    //商票正面图片预览地址
    private String ticketFrontImgViewUrl;

    //商票正面图片下载地址
    private String ticketFrontImgDownloadUrl;

    //商票反面图片预览地址
    private String ticketBackImgViewUrl;

    //商票反面图片下载地址
    private String ticketBackImgDownloadUrl;

    public String getTicketFrontImgViewUrl() {
        return FileUtil.getViewFileUrl(this.ticketFrontImgUrl);
    }

    public String getTicketBackImgViewUrl() {
        return FileUtil.getViewFileUrl(this.ticketBackImgUrl);
    }

    public String getTicketFrontImgDownloadUrl() {
        return FileUtil.getDownloadFileUrl(this.ticketFrontImgUrl);
    }

    public String getTicketBackImgDownloadUrl() {
        return FileUtil.getDownloadFileUrl(this.ticketBackImgUrl);
    }
}
