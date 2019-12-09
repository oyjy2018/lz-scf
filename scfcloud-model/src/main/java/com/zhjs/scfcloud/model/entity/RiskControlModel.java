package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-15 16:02
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_risk_control_model")
public class RiskControlModel {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 模型名称
     * model_name
     */
    private String modelName;

    /**
     * 所属系统平台
     * system_id
     */
    private Long systemId;

    /**
     * 所属业务ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 所属业务
     * business_type_name
     */
    private String businessTypeName;

    /**
     * 模型公式
     * model_formula
     */
    private String modelFormula;

    /**
     * 模型说明
     * model_explain
     */
    private String modelExplain;

    /**
     * 是否启用（0：否；1：是）
     * has_enabled
     */
    private Integer hasEnabled;

    /**
     * 是否有效（0：否；1：是）
     * status
     */
    private Integer status;

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