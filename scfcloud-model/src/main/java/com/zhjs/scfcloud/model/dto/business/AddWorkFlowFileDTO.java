package com.zhjs.scfcloud.model.dto.business;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dailongting
 * @date:2019/9/24 16:43
 */
@Data
@Accessors(chain = true)
public class AddWorkFlowFileDTO {

    /**
     * 流程附件配置ID（修改时使用，前端不传）
     */
    private Long workFlowFileId;

    /**
     * 附件配置ID
     */
    private Long businessFileId;

    /**
     * 是否必填
     */
    private Integer required;

    private Integer sort;
}
