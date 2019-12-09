package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-10-24 11:23
 */
@Data
public class LzProjectItemInfoVO {
    /**
     * id
     */
    private Long id;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 所属商务经理
     * business_manager
     */
    private String businessManager;


    /**
     * 计划开工日期
     * plan_commence_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date planCommenceDate;

    /**
     * 计划完工日期
     * plan_complete_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date planCompleteDate;


    /**
     * 所属分公司
     * branch_company
     */
    private String branchCompany;

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

}