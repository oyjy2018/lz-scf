package com.zhjs.scfcloud.model.vo.businessTicket;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.util.enums.BusinessTicketEnum;
import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;

import java.util.Optional;

/**
 * @author: dailongting
 * @date:2019/7/17 14:39
 */
@Data
public class OrderAllListVO {
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

    //票据号码
    private String billNo;

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
     * 接受报价时间
     * accepted_time
     */
    private String dealTime;

    /**
     * 合同在本地系统保存的地址
     */
    @JSONField(serialize = false)
    private String contractFileUrl;

    private String contractFileUrlView;

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

}
