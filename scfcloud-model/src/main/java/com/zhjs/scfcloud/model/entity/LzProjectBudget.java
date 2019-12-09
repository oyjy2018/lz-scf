package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-31 11:20
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_budget")
public class LzProjectBudget {
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
     * 项目预算编号
     * project_budget_no
     */
    private String projectBudgetNo;

    /**
     * 预算创建日期
     * budget_create_date
     */
    private Date budgetCreateDate;

    /**
     * 材料费比例%
     * materials_cost_rate
     */
    private BigDecimal materialsCostRate;

    /**
     * 人工费比列%
     * labor_cost_rate
     */
    private BigDecimal laborCostRate;

    /**
     * 项目管理人员薪酬比例%
     * executive_salary_rate
     */
    private BigDecimal executiveSalaryRate;

    /**
     * 项目费用比例%
     * project_cost_rate
     */
    private BigDecimal projectCostRate;

    /**
     * 咨询服务费比例%
     * consult_coast_rate
     */
    private BigDecimal consultCoastRate;

    /**
     * 综合税费比例%
     * comprehensive_taxes_rate
     */
    private BigDecimal comprehensiveTaxesRate;

    /**
     * 平台服务费缴纳比例%
     * platform_service_rate
     */
    private BigDecimal platformServiceRate;

    /**
     * 商务服务费比例%
     * commerce_service_rate
     */
    private BigDecimal commerceServiceRate;

    /**
     * 金融服务费比例%
     * finance_service_rate
     */
    private BigDecimal financeServiceRate;

    /**
     * 技术服务费比例%
     * skill_service_rate
     */
    private BigDecimal skillServiceRate;

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