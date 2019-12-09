package com.zhjs.scfcloud.model.vo.lzProject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-14 16:00
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_invoice")
public class LzProjectInvoiceListVO {
    /**
     * 
     * id
     */
    private Long id;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
}