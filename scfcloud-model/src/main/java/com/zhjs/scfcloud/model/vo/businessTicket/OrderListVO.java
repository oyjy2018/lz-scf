package com.zhjs.scfcloud.model.vo.businessTicket;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author: dailongting
 * @date:2019/7/17 14:39
 */
@Data
public class OrderListVO {
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

    //合同签署状态（个位:乙方公司,十位:乙方授权人,百位:甲方公司,千位:甲方个人;每位上数字为1时表示已签,0或空为未签）
    @JSONField(serialize = false)
    private Integer contractSignStatus;

    //授权人是否签署
    private boolean hasUserSign;

    //公司是否签署
    private boolean hasCompanySign;

    /**
     * 订单状态
     * order_status
     */
    private Integer orderStatus;

    private String orderStatusCH;

    /**
     * 票据状态（0：未背书；1：背书代签收；2：背书已签收）
     * bill_status
     */
    private Integer billStatus;

    private String billStatusCH;

    /**
     * 支付状态
     * pay_status
     */
    private Integer payStatus;

    private String payStatusCH;

    /**
     * 票方（卖方）公司ID
     * company_id
     */
    private Long inquireCompanyId;

    /**
     * 票方（卖方）公司名称
     * company_name
     */
    private String inquireCompanyName;

    /**
     * 资方（买方）公司ID
     * company_id
     */
    private Long quotationCompanyId;

    /**
     * 资方（买方）公司名称
     * company_name
     */
    private String quotationCompanyName;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 票面金额
     * bill_amt
     */
    private String billAmt;

    /**
     * 成交价格
     * amount
     */
    private String amount;

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
     * 票据瑕疵
     * flaws
     */
    private String flaws;

    /**
     * 票方（卖方）签约操作人ID
     * publish_person_id
     */
    private Long publishPersonId;

    /**
     * 票方（卖方）签约操作人
     * publish_person
     */
    private String publishPerson;

    /**
     * 资方（买方）签约操作人ID
     * quotation_person_id
     */
    private Long quotationPersonId;

    /**
     * 资方（买方）签约操作人
     * quotation_person
     */
    private String quotationPerson;

    /**
     * 接受报价时间
     * accepted_time
     */
    private String dealTime;

    /**
     * 合同在本地系统保存的地址
     */
    private String contractFileUrl;

    private String contractFileUrlView;

    //合同类型（0：系统生成，1：用户上传）
    private String contractType;

    public String getOrderStatusCH() {
        Optional<BusinessTicketEnum.OrderStatus> optional = BusinessTicketEnum.OrderStatus.getEnumByStatus(this.orderStatus);
        return optional.isPresent() ?optional.get().getStatusCH():"";
    }

    public String getBillStatusCH() {
        return BusinessTicketEnum.BillStatus.getStatusCH(this.billStatus);
    }

    public String getPayStatusCH() {
        Optional<BusinessTicketEnum.OrderPayStatus> optional = BusinessTicketEnum.OrderPayStatus.getEnumByStatus(this.payStatus);
        return optional.isPresent() ?optional.get().getStatusCH():"";
    }

    public String getContractFileUrlView() {
        return FileUtil.getViewFileUrl(this.contractFileUrl);
    }

    public boolean isHasUserSign() {
        //待买（乙）方签署时判断
        if (this.orderStatus.intValue() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()) {
            return this.contractSignStatus%100 >= 10;
        }
        //待卖（甲）方签署时判断
        if (this.orderStatus.intValue() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()) {
            return this.contractSignStatus >= 1000;
        }
        return hasUserSign;
    }

    public boolean isHasCompanySign() {
        //待买方签署时判断
        if (this.orderStatus.intValue() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_1.getStatus().intValue()) {
            return this.contractSignStatus%10 == 1;
        }
        //待卖方签署时判断
        if (this.orderStatus.intValue() == BusinessTicketEnum.OrderStatus.ORDER_STATUS_2.getStatus().intValue()) {
            return this.contractSignStatus%1000 >= 100;
        }
        return hasCompanySign;
    }
}
