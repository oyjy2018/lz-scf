package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-30 09:29
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_pay")
public class LzProjectPay {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 关联合同编号(劳务合同、材料合同等)
     * related_contract_no
     */
    private String relatedContractNo;

    /**
     * 付款登记编号
     * pay_register_no
     */
    private String payRegisterNo;

    /**
     * 付款日期
     * pay_date
     */
    private Date payDate;

    /**
     * 付款类型（人工费、材料费、税费、平台服务费、管理费、项目费用、其他... ）
     * pay_type
     */
    private String payType;

    /**
     * 付款方全称
     * payer_full_name
     */
    private String payerFullName;

    /**
     * 收款方全称
     * gathering_full_name
     */
    private String gatheringFullName;

    /**
     * 实际付款金额
     * actual_pay_money
     */
    private BigDecimal actualPayMoney;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

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
}