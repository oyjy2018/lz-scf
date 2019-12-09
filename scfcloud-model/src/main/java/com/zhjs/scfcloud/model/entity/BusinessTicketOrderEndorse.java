package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-21 14:27
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_order_endorse")
public class BusinessTicketOrderEndorse {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 订单ID
     * order_id
     */
    private Long orderId;

    /**
     * 票号
     * bill_no
     */
    private String billNo;

    /**
     * 票面到期日
     * bill_end_date
     */
    private Date billEndDate;

    /**
     * 票面金额
     * bill_amt
     */
    private BigDecimal billAmt;

    /**
     * 背书人
     * endorser_name
     */
    private String endorserName;

    /**
     * 背书人账号
     * endorser_account
     */
    private String endorserAccount;

    /**
     * 被背书人
     * endorsee_name
     */
    private String endorseeName;

    /**
     * 背背书人账号
     * endorsee_account
     */
    private String endorseeAccount;

    /**
     * 背书时间
     * endorse_date
     */
    private Date endorseDate;

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
}