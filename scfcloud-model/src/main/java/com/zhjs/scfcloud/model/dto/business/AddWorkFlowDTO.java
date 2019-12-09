package com.zhjs.scfcloud.model.dto.business;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/9/19 14:00
 */
@Data
@Accessors(chain = true)
public class AddWorkFlowDTO {
    private String flowCode;

    private List<String> flowExtendList;

}
