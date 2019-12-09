package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ScoreItemFormulaEditVO {


    /**
     * 条件类型
     * criteria_type
     */
    @NotNull(message = "条件类型不能为空")
    private Integer criteriaType;

    /**
     * 条件json集合（格式是数组[]）
     * criteria_jsons
     */
    @NotNull(message = "条件不能为空")
    private String criteriaJsons;

    /**
     * 条件集合说明
     * criteria_jsons_desc
     */
    private String criteriaJsonsDesc;

    /**
     * 公式
     * formula
     */
    @NotNull(message = "公式不能为空")
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

}
