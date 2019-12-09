package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-14 14:52
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_contract_sub")
public class LzProjectContractSub {
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
     * 子合同号
     * sub_contract_no
     */
    private String subContractNo;

    /**
     * 合同类型（1：劳务合同，2：采购合同）
     * contract_type
     */
    private Integer contractType;

    /**
     * 甲方全称
     * first_party_full_name
     */
    private String firstPartyFullName;

    /**
     * 乙方全称
     * second_party_full_name
     */
    private String secondPartyFullName;

    /**
     * 合同金额
     * contract_money
     */
    private BigDecimal contractMoney;

    /**
     * 付款方式
     * pay_way
     */
    private String payWay;

    /**
     * 发票类型
     * invoice_type
     */
    private String invoiceType;

    /**
     * 合同开始日期
     * contract_begin_date
     */
    private Date contractBeginDate;

    /**
     * 合同结束日期
     * contract_end_date
     */
    private Date contractEndDate;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

    /**
     * 合同地址
     * file_url
     */
    private String fileUrl;

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
