package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-08-26 17:36
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_record")
public class CreditRecord {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 公司id
     * company_id
     */
    private Long companyId;

    /**
     * 授信申请ID
     * credit_apply_id
     */
    private Long creditApplyId;

    /**
     * 授信人身份证号
     * id_card
     */
    private String idCard;

    /**
     * 授信人姓名
     * customer_name
     */
    private String customerName;

    /**
     * 项目ID
     * project_id
     */
    private Long projectId;

    /**
     * 项目名称
     * project_name
     */
    private String projectName;

    /**
     * 授信金额
     * credit_amount
     */
    private BigDecimal creditAmount;

    /**
     * 已用授信金额
     * used_credit_amount
     */
    private BigDecimal usedCreditAmount;

    /**
     * 授信开始日期
     * credit_start
     */
    private Date creditStart;

    /**
     * 授信结束日期
     * credit_end
     */
    private Date creditEnd;

    /**
     * 关联工程云系统项目id
     * relate_project_id
     */
    private String relateProjectId;

    /**
     * 是否为人工导入，0:不是,1:是.
     * if_import
     */
    private Integer ifImport;

    /**
     * 软删状态 0：未删 1：已删
     * delete_status
     */
    private Integer deleteStatus;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
}