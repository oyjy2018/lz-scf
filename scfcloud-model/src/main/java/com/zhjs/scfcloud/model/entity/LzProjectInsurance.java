package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-13 14:08
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_insurance")
public class LzProjectInsurance {
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
     * 保单编号
     * insurance_no
     */
    private String insuranceNo;

    /**
     * 保险类型
     * insurance_type
     */
    private String insuranceType;

    /**
     * 保险到期日期
     * insurance_expire_date
     */
    private Date insuranceExpireDate;

    /**
     * 团体险人数
     * groups_count
     */
    private Integer groupsCount;

    /**
     * 平均每人保额
     * per_capita_coverage
     */
    private BigDecimal perCapitaCoverage;

    /**
     * 总额度
     * total_coverage
     */
    private BigDecimal totalCoverage;

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