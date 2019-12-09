package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目列表 vo
 */

@Data
public class LzProjectContractVO {

    /**
     * 项目名称（合约名称）
     * project_name
     */
    private String projectName;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 子合同号
     * sub_contract_no
     */
    private String subContractNo;

    /**
     * 合同类型（1：劳务合同，2：采购合同）
     * contract_type
     */
    private Integer contractType;

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

    /**
     * 合同金额
     * contract_money
     */
    private BigDecimal contractMoney;

    /**
     * 合同开始日期
     * contract_begin_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date contractBeginDate;

    /**
     * 合同结束日期
     * contract_end_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date contractEndDate;

    /**
     * 合同地址
     * file_url
     */
    private String fileUrl;

}
