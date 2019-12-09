package com.zhjs.scfcloud.model.vo.lzProject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款信息列表 vo
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_pay")
public class LzProjectPayListVO {
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
     * 关联合同编号(劳务合同、材料合同等)
     * related_contract_no
     */
    private String relatedContractNo;

    /**
     * 付款登记编号
     * pay_register_no
     */
    private String payRegisterNo;

    /**
     * 付款日期
     * pay_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date payDate;

    /**
     * 付款类型（人工费、材料费、税费、平台服务费、管理费、项目费用、其他... ）
     * pay_type
     */
    private String payType;

    /**
     * 付款方全称
     * payer_full_name
     */
    private String payerFullName;

    /**
     * 收款方全称
     * gathering_full_name
     */
    private String gatheringFullName;

    /**
     * 实际付款金额
     * actual_pay_money
     */
    private BigDecimal actualPayMoney;

}