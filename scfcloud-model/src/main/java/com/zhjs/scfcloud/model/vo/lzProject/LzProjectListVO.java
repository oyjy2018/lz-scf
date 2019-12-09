package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 项目列表 vo
 */

@Data
public class LzProjectListVO {
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
     * 项目名称（合约名称）
     * project_name
     */
    private String projectName;

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
     * 项目类型
     * project_type
     */
    private String projectType;

    /**
     * 甲方全称
     * first_party_full_name
     */
    private String firstPartyFullName;

    /**
     * 乙方全称
     * second_party_full_name
     */
    private String secondPartyFullName;

}