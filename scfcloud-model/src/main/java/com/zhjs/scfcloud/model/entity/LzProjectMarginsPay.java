package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-01 11:50
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_margins_pay")
public class LzProjectMarginsPay {
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
     * 保证金编号
     * margins_no
     */
    private String marginsNo;

    /**
     * 缴纳日期
     * pay_date
     */
    private Date payDate;

    /**
     * 缴纳金额
     * pay_money
     */
    private BigDecimal payMoney;

    /**
     * 保证金类型
     * margins_type
     */
    private String marginsType;

    /**
     * 保证金状态（已缴纳、付甲方、已提取）
     * margins_status
     */
    private String marginsStatus;

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