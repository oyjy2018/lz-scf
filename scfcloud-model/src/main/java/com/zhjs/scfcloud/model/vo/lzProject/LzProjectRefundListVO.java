package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-01 16:46
 */
@Data
public class LzProjectRefundListVO {
    /**
     * id
     */
    private Long id;


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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date refundDate;

    /**
     * 还款金额
     * refund_money
     */
    private BigDecimal refundMoney;

}