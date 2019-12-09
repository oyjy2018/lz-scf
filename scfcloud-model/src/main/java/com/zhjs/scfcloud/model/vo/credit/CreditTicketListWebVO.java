package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.model.enums.CreditUseEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商票详情vo web
 */

@Data
public class CreditTicketListWebVO {

    //商票id
    private Long id;

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
    private Integer ticketStatus;

    //票据状态名
    private String ticketStatusName;

    /**
     * 票据金额
     * ticket_money
     */
    private BigDecimal ticketMoney;


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

    public String getTicketStatusName() {
        return CreditUseEnum.TicketStatus.getName(this.ticketStatus);
    }
}
