package com.zhjs.scfcloud.model.vo.risk;

import lombok.Data;

/**
 * 评分项公式参数
 */
@Data
public class RiskControlVariableVO {
    /**
     * 列名
     * column_name
     */
    private String columnName;

    /**
     * 列展示名(页面名)
     * column_ch
     */
    private String columnCh;
    /**
     * 字段类型
     * column_type
     */
    private String columnType;

}
