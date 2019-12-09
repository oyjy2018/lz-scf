package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-01 13:44
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_pledge_cash_pay")
public class LzProjectPledgeCashPay {
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
     * 押金编号
     * pledge_cash_no
     */
    private String pledgeCashNo;

    /**
     * 缴纳日期
     * pay_date
     */
    private Date payDate;

    /**
     * 押金金额
     * pay_money
     */
    private BigDecimal payMoney;

    /**
     * 押金金额
     * pledge_cash_type
     */
    private String pledgeCashType;

    /**
     * 押金状态（已缴纳、付甲方、已提取）
     * pledge_cash_status
     */
    private String pledgeCashStatus;

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