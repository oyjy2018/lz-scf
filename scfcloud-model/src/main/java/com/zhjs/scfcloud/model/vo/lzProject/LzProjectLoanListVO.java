package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-04 16:05
 */
@Data
public class LzProjectLoanListVO {
    /**
     * id
     */
    private Long id;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 借款人姓名
     * debtor_name
     */
    private String debtorName;

    /**
     * 借款编号
     * loan_no
     */
    private String loanNo;

    /**
     * 放款日期
     * loan_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;

    /**
     * 借款类型
     * loan_type
     */
    private String loanType;

    /**
     * 借款金额
     * loan_money
     */
    private BigDecimal loanMoney;

    /**
     * 已还本金
     * has_refund_money
     */
    private BigDecimal hasRefundMoney;

    /**
     * 应结清日
     * should_settle_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shouldSettleDate;

    /**
     * 是否结清
     * is_settle
     */
    private String isSettle;

    /**
     * 资金端
     * money_source
     */
    private String moneySource;
}