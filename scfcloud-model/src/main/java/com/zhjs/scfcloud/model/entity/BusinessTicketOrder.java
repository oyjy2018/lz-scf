package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-27 18:15
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_order")
public class BusinessTicketOrder {
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
     * 订单号
     * order_no
     */
    private String orderNo;

    /**
     * 平台请求流水号(订单在京东智票唯一标识)
     * platform_req_no
     */
    private String platformReqNo;

    /**
     * 京东智票订单号
     * jd_order_no
     */
    private String jdOrderNo;

    /**
     * 合同号
     * contract_no
     */
    private String contractNo;

    /**
     * 合同文件在领筑互联系统生成的key
     * contract_file_key
     */
    //private String contractFileKey;

    /**
     * 合同文件在本系统的保存地址
     * contract_file_url
     */
    //private String contractFileUrl;

    /**
     * 交易金额
     * amounts
     */
    private BigDecimal amounts;

    /**
     * 确定交易时剩余天数
     * surplus_valid_days
     */
    private Integer surplusValidDays;

    /**
     * 合同签署状态（个位:乙方公司,十位:乙方授权人,百位:甲方公司,千位:甲方个人;每位上数字为1时表示已签,0或空为未签）
     * contract_sign_status
     */
    private Integer contractSignStatus;

    /**
     * 订单状态（1：待买家签合同；2：待卖家签合同；3：双方已签合同-待支付；4：支付成功-待背书；5：已背书-待签收；6：已背书-已签收；99：交易成功；97：撤销中；98：已撤销）
     * order_status
     */
    private Integer orderStatus;

    /**
     * 支付状态（1：支付中；97：支付撤销中；98：支付失败；99：支付成功；）
     * pay_status
     */
    private Integer payStatus;

    /**
     * 支付发起时间
     * pay_launch_time
     */
    private Date payLaunchTime;

    /**
     * 支付过期时间
     * pay_overdue_time
     */
    private Date payOverdueTime;

    /**
     * 支付结束时间
     * pay_end_time
     */
    private Date payEndTime;

    /**
     * 打款给卖方状态（1：打款给卖方中；98：打款失败；99：打款成功；）
     * remit_status
     */
    private Integer remitStatus;

    /**
     * 佣金扣除状态（1：扣除平台佣金中；98：平台佣金扣除失败；99：平台佣金扣除成功；）
     * comm_deduct_status
     */
    private Integer commDeductStatus;

    /**
     * 交易成功时间
     * deal_time
     */
    private LocalDateTime dealTime;

    /**
     * 取消类型(1:买方取消;2:卖方取消;)
     * cancel_type
     */
    private Integer cancelType;

    /**
     * 取消人ID
     * cancel_person_id
     */
    private Long cancelPersonId;

    /**
     * 取消人
     * cancel_person
     */
    private String cancelPerson;

    /**
     * 取消时间
     * cancel_time
     */
    private Date cancelTime;

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

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;

    /**
     * 
     * service_rate
     */
    private BigDecimal serviceRate;
}