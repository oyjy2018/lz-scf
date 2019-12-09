package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-14 16:00
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_invoice")
public class LzProjectInvoice {
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
     * 发票编号
     * invoice_no
     */
    private String invoiceNo;

    /**
     * 发票分类（工程发票（预缴发票）、劳务发票、材料发票）
     * invoice_type
     */
    private String invoiceType;

    /**
     * 登记类型（开出/收到）
     * register_type
     */
    private String registerType;

    /**
     * 发票日期
     * invoice_date
     */
    private Date invoiceDate;

    /**
     * 发票金额
     * invoice_money
     */
    private BigDecimal invoiceMoney;

    /**
     * 开发票公司全称
     * invoicing_company_full_name
     */
    private String invoicingCompanyFullName;

    /**
     * 备注
     * remark
     */
    private String remark;

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