package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ScoreItemVO {
    /**
     * 风控模型ID
     * risk_control_model_id
     */
    @NotNull(message = "风控模型ID不能为空")
    private Long riskControlModelId;

    /**
     * 评分项名称
     * item_name
     */
    @NotNull(message = "评分项名称不能为空")
    private String itemName;

    /**
     * 满分值
     * full_mark
     */
    @NotNull(message = "满分值不能为空")
    @DecimalMin(value = "0",message = "满分值必须大于或等于0")
    @DecimalMax(value = "10000",message = "满分值必须小于或等于10000")
    private BigDecimal fullMark;

    /**
     * 评分项说明
     * item_explain
     */
    @NotNull(message = "评分项说明不能为空")
    private String itemExplain;

    /**
     * 评分项公式
     * formula
     */
    @NotNull(message = "评分项公式不能为空")
    private String formula;

    /**
     * 评分项公式说明
     * formula_desc
     */
    private String formulaDesc;

    @NotEmpty(message = "平风项列表不能为空")
    private List<ScoreItemFormulaVO> formulaList;

}
