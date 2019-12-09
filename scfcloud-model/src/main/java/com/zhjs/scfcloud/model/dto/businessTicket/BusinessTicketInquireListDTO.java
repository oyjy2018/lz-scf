package com.zhjs.scfcloud.model.dto.businessTicket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.annotation.IsLong;
import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 询价（商票）列表 参数
 */
@Data
public class BusinessTicketInquireListDTO extends Page {

    //询价单id
    @IsLong
    private String id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 票面金额-起始值
     * bill_amt
     */
    private String billAmtBegin;

    /**
     * 票面金额-结束值
     * bill_amt
     */
    private String billAmtEnd;

    /**
     * 剩余天数-起始值
     * surplus_valid_days
     */
    private String surplusValidDaysBegin;

    /**
     * 剩余天数-结束值
     * surplus_valid_days
     */
    private String surplusValidDaysEnd;

    /**
     * 有无瑕疵
     * 0：无瑕疵 1：有瑕疵
     */
    private String hasFlaws;

    /** 发布时间-起始值
     * create_time
     */
    private String createTimeBegin;

    /** 发布时间-结束值
     * create_time
     */
    private String createTimeEnd;

    //询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
    private String status;

    //多个状态
    private String multiStatus;

    //是否过滤掉指定卖家的
    private String isAssignBuyer;

    public String getBillAmtBegin() {
        //不为空且是数字
        if (!StringUtil.isEmpty(billAmtBegin)&&StringUtil.isDigit(billAmtBegin)) {
            //乘1W
            return new BigDecimal(billAmtBegin).multiply(new BigDecimal(10000)).toString();
        }
        return billAmtBegin;
    }

    public String getBillAmtEnd() {
        //不为空且是数字
        if (!StringUtil.isEmpty(billAmtEnd)&&StringUtil.isDigit(billAmtEnd)) {
            //乘1W
            return new BigDecimal(billAmtEnd).multiply(new BigDecimal(10000)).toString();
        }
        return billAmtEnd;
    }
}