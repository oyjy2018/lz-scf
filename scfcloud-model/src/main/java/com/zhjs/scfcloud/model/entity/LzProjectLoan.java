package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-04 16:05
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_loan")
public class LzProjectLoan {
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
    private Date shouldSettleDate;

    /**
     * 是否结清
     * is_settle
     */
    private String isSettle;

    /**
     * 是否为项目经理个人借款（0:不是，1:是）
     * is_personal_loan
     */
    private Integer isPersonalLoan;

    /**
     * 资金端
     * money_source
     */
    private String moneySource;

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