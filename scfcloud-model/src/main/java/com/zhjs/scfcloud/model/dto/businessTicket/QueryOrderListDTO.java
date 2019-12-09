package com.zhjs.scfcloud.model.dto.businessTicket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.annotation.IsLong;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/7/17 16:13
 */
@Data
public class QueryOrderListDTO extends Page {

    /**
     * 用户类型（1：票方；2：资方）
     */
    @NotNull(message = "用户类型不能为空")
    public Integer userType;

    /**
     * 订单ID
     */
    @IsLong(message = "订单id必须是数字")
    private String orderId;

    /**
     * 订单状态
     * order_status
     */
    private Integer orderStatus;

    /**
     * 票据状态（0：未背书；1：背书代签收；2：背书已签收）
     * bill_status
     */
    private Integer billStatus;

    /**
     * 支付状态
     * pay_status
     */
    private Integer payStatus;

    /**
     * 成交时间-起
     */
    private String dealStartDate;

    /**
     * 成交时间-止
     */
    private String dealEndDate;

    /**
     * 发布人ID
     * publish_person_id
     */
    private Long publishCompanyId;

    /**
     * 票方公司名称
     * publish_person
     */
    private String publishCompanyName;

    /**
     * 报价人ID
     * quotation_person_id
     */
    private Long quotationCompanyId;

    /**
     * 资方公司名称
     * quotation_person
     */
    private String quotationCompanyName;

    //承兑方名称
    private String acceptorName;

    //商票是否到期
    private String isTicketMaturity;
}
