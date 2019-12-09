package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-10-24 17:55
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_risk_scoring_items_formula")
public class ScoreItemsFormula {
    /**
     *
     * id
     */
    private Long id;

    /**
     * 评分项ID
     * scoring_items_id
     */
    private Long scoringItemsId;

    /**
     * 条件类型
     * criteria_type
     */
    private Integer criteriaType;

    /**
     * 条件json集合（格式是数组[]）
     * criteria_jsons
     */
    private String criteriaJsons;

    /**
     * 条件集合说明
     * criteria_jsons_desc
     */
    private String criteriaJsonsDesc;

    /**
     * 总公式
     * formula
     */
    private String formula;

    /**
     * 公式说明
     * formula_desc
     */
    private String formulaDesc;

    /**
     * 原公式
     * formula_desc_pre
     */
    private String formulaDescPre;

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
