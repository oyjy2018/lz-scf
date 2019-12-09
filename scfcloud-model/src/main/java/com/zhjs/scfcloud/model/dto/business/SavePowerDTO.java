package com.zhjs.scfcloud.model.dto.business;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/9/24 16:35
 */
@Data
@Accessors(chain = true)
public class SavePowerDTO extends WorkFlowExistsDTO{

    /**
     * 流程扭转字段配置
     */
    private List<AddWorkFlowAttrDTO> workFlowAttrList;

    /**
     * 流程扭转附件配置
     */
    private List<AddWorkFlowFileDTO> workFlowFileList;

    /**
     * 流程扭转用户组权限
     */
    private List<String> roleIds;

    /**
     * 流程扭转用户权限
     */
    private List<String> userIds;

    private Long userId;
}
