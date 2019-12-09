package com.zhjs.scfcloud.model.dto.business;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dailongting
 * @date:2019/9/24 16:38
 */
@Data
@Accessors(chain = true)
public class AddWorkFlowAttrDTO {

    /**
     * 流程字段配置ID（修改时使用，前端不传）
     */
    private Long workFlowAttrId;
    /**
     * 字段配置ID
     */
    private Long businessAttrId;

    /**
     * 默认值类型
     */
    private String defaultValueType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否必填
     */
    private Integer required;

    private Integer sort;
}
