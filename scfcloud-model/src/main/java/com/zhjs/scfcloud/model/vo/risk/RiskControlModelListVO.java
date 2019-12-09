package com.zhjs.scfcloud.model.vo.risk;

import com.zhjs.scfcloud.util.enums.CommonEnum;
import lombok.Data;

/**
 * 风控模型列表 vo
 */
@Data
public class RiskControlModelListVO {
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

    private String companyName;

    /**
     * 模型名称
     * model_name
     */
    private String modelName;

    //所属平台
    private String systemName;

    /**
     * 所属业务
     * business_type_name
     */
    private String businessTypeName;


    /**
     * 是否启用（0：否；1：是）
     * has_enabled
     */
    private Integer hasEnabled;

    /**
     * 是否启用说明
     * has_enabled
     */
    private String hasEnabledCH;

    public String getHasEnabledCH() {
        if (CommonEnum.WhetherEnum.YES.getStatus().intValue() == hasEnabled) {
            return "已启用";
        }
        return "已禁用";
    }
}
