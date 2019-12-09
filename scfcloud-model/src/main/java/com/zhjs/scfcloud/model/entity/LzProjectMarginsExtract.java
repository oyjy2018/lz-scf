package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-01 13:29
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_margins_extract")
public class LzProjectMarginsExtract {
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
     * 提取保证金编号
     * extract_margins_no
     */
    private String extractMarginsNo;

    /**
     * 对应的缴纳保证金编号
     * margins_no
     */
    private String marginsNo;

    /**
     * 缴纳日期
     * extract_date
     */
    private Date extractDate;

    /**
     * 缴纳金额
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