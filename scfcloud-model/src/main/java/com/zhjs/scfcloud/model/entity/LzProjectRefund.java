package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-01 16:46
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_refund")
public class LzProjectRefund {
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
     * 关联借款单编号
     * loan_no
     */
    private String loanNo;

    /**
     * 还款编号
     * refund_no
     */
    private String refundNo;

    /**
     * 还款日期
     * refund_date
     */
    private Date refundDate;

    /**
     * 还款金额
     * refund_money
     */
    private BigDecimal refundMoney;

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