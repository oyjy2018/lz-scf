package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-13 14:08
 */
@Data
public class LzProjectInsuranceListVO {
    /**
     * 
     * id
     */
    private Long id;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
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

}