package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-01 16:27
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_pledge_cash_extract")
public class LzProjectPledgeCashExtract {
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
     * 提取编号
     * extract_no
     */
    private String extractNo;

    /**
     * 押金编号
     * pledge_cash_no
     */
    private String pledgeCashNo;

    /**
     * 提取日期
     * extract_date
     */
    private Date extractDate;

    /**
     * 提取金额
     * extract_money
     */
    private BigDecimal extractMoney;

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
