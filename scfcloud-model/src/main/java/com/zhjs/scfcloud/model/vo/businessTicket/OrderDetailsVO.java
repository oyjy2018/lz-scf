package com.zhjs.scfcloud.model.vo.businessTicket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.model.entity.BusinessTicketFile;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: dailongting
 * @date:2019/7/18 9:26
 */
@Data
public class OrderDetailsVO {
    /**
     *
     * id
     */
    private Long id;

    /**
     * 询价信息ID
     * inquire_id
     */
    private Long inquireId;

    /**
     * 报价信息ID
     * quotation_id
     */
    private Long quotationId;

    /**
     * 卖方（票方）公司名称
     * company_name
     */
    private String inquireCompanyName;

    /**
     * 卖方（票方）联系人
     * publish_person
     */
    private String publishPerson;

    /**
     * 卖方（票方）联系电话
     */
    private String publishPersonPhone;

    /**
     * 买方（资方）公司
     * company_id
     */
    private String quotationCompanyName;

    /**
     * 买方（资方）联系人
     * quotation_person
     */
    private String quotationPerson;

    /**
     * 买方（资方）联系电话
     * quotation_person
     */
    private String quotationPersonPhone;

    /**
     * 交易价格
     * amount
     */
    private String amount;

    /**
     * 年化率
     * annual_rate
     */
    private String annualRate;

    /**
     * 每十万扣款
     * discount_fee
     */
    private String discountFee;

    /**
     * 订单状态
     * order_status
     */
    private Integer orderStatus;

    private String orderStatusCH;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 票据号码
     */
    private String billNo;

    /**
     * 票面金额
     * bill_amt
     */
    private String billAmt;

    /**
     * 到期日
     * maturity_date
     */
    private String maturityDate;

    /**
     * 确定交易时剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;

    /**
     * 背书次数
     */
    private Integer endorsedCount;

    /**
     * 票据瑕疵
     */
    private String flaws;

    /**
     * 订单创建时间
     */
    private String createTime;

    /**
     * 票据图片
     */
    private List<BusinessTicketFile> ticketUrlList;

    public String getOrderStatusCH() {
        Optional<BusinessTicketEnum.OrderStatus> optional = BusinessTicketEnum.OrderStatus.getEnumByStatus(this.orderStatus);
        return optional.isPresent() ?optional.get().getStatusCH():"";
    }
}
