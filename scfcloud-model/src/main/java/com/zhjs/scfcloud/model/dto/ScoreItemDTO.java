package com.zhjs.scfcloud.model.dto;

import com.zhjs.scfcloud.model.entity.ScoreItemsFormula;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ScoreItemDTO {
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
     * 评分项说明
     * item_explain
     */
    private String itemExplain;

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

    private List<ScoreItemsFormula> formulaList;

}
