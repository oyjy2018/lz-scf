package com.zhjs.scfcloud.model.vo.businessTicket;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 我的发布vo
 */
@Data
public class BusinessTicketInquireListVO {
    /**
     * 
     * id
     */
    private Long id;


    /**
     * 票面金额
     * bill_amt
     */
    private BigDecimal billAmt;


    /**
     * 到期日
     * maturity_date
     */
    //@JSONField(format="yyyy-MM-dd")
    private LocalDate maturityDate;


    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;


    /**
     * 票据瑕疵
     * flaws
     */
    private String flaws;


    /**
     * 询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
     * status
     */
    private Integer status;

    /**
     * 询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
     * status
     */
    private String statusCH;


    //关联报价id
    private Long relatedQuotationId;

    //创建时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //发布操作人
    private String publishPerson;

    public String getStatusCH() {
        return BusinessTicketEnum.InquireStatus.getStatusCH(this.status);
    }
}