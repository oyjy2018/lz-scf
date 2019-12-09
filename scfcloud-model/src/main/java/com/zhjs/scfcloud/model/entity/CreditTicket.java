package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;

import com.jd.jr.cbp.sdk.entity.ocr.response.OcrRespBody;
import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-01 15:09
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_ticket")
public class CreditTicket {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 公司id
     * company_id
     */
    private Long companyId;

    /**
     * 用信id
     * credit_use_id
     */
    private Long creditUseId;

    /**
     * 出票日期
     * ticket_start
     */
    private Date ticketStart;

    /**
     * 到期日期
     * ticket_end
     */
    private Date ticketEnd;

    /**
     * 票据号码
     * ticket_no
     */
    private String ticketNo;

    /**
     * 票据状态
     * ticket_status
     */
    private Integer ticketStatus;

    /**
     * 票据金额
     * ticket_money
     */
    private BigDecimal ticketMoney;

    /**
     * 能否转让(0:否,1:能)
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

    //正面图片
    private String ticketFrontImgUrl;

    //背面图片
    private String ticketBackImgUrl;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 更新人
     * update_by
     */
    private Long updateBy;

    /**
     * 更新时间
     * update_time
     */
    private Date updateTime;

    /**
     * 状态
     * status
     */
    private Integer status;
}