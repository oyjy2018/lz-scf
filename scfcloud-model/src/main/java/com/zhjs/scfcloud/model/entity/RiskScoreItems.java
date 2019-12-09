package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-10-23 15:37
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_risk_scoring_items")
public class RiskScoreItems {
    /**
     *
     * id
     */
    private Long id;

    /**
     * 风控模型ID
     * risk_control_model_id
     */
    private Long riskControlModelId;

    /**
     * 评分项名称
     * item_name
     */
    private String itemName;

    /**
     * 满分值
     * full_mark
     */
    private BigDecimal fullMark;

    /**
     * 评分项公式
     * formula
     */
    private String formula;

    /**
     * 评分项公式说明
     * formula_desc
     */
    private String formulaDesc;

    /**
     * 评分项说明
     * item_explain
     */
    private String itemExplain;

    /**
     * 是否有效(0:否;1:是)
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
