package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-24 11:23
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_basic_info")
public class LzProjectBasicInfo {
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
     * 项目名称（合约名称）
     * project_name
     */
    private String projectName;

    /**
     * 所属分公司
     * branch_company
     */
    private String branchCompany;

    /**
     * 所属商务经理
     * business_manager
     */
    private String businessManager;

    /**
     * 项目提交到工程经营系统的时间
     * submit_time
     */
    private Date submitTime;

    /**
     * 计划开工日期
     * plan_commence_date
     */
    private Date planCommenceDate;

    /**
     * 计划完工日期
     * plan_complete_date
     */
    private Date planCompleteDate;

    /**
     * 实际开工日期
     * actual_commence_date
     */
    private Date actualCommenceDate;

    /**
     * 实际结束日期
     * actual_complete_date
     */
    private Date actualCompleteDate;

    /**
     * 项目状态（报备中、建设中、已竣工）
     * project_staus
     */
    private String projectStatus;

    /**
     * 经营模式（联营/自营）
     * business_model
     */
    private String businessModel;

    /**
     * 项目地址-省
     * project_province
     */
    private String projectProvince;

    /**
     * 项目地址-市
     * project_city
     */
    private String projectCity;

    /**
     * 项目地址-区/县
     * project_region
     */
    private String projectRegion;

    /**
     * 项目地址
     * peoject_address
     */
    private String projectAddress;

    /**
     * 项目类型
     * project_type
     */
    private String projectType;

    /**
     * 项目造价
     * project_cost
     */
    private BigDecimal projectCost;

    /**
     * 建筑类型
     * building_type
     */
    private String buildingType;

    /**
     * 建筑面积
     * building_area
     */
    private BigDecimal buildingArea;

    /**
     * 进度付款比例
     * progress_payment_ratio
     */
    private BigDecimal progressPaymentRatio;

    /**
     * 工程类型(新建/改建)
     * engineering_type
     */
    private String engineeringType;

    /**
     * 甲方全称
     * first_party_full_name
     */
    private String firstPartyFullName;

    /**
     * 甲方地址
     * first_party_address
     */
    private String firstPartyAddress;

    /**
     * 乙方全称
     * second_party_full_name
     */
    private String secondPartyFullName;

    /**
     * 乙方地址
     * second_party_address
     */
    private String secondPartyAddress;

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
