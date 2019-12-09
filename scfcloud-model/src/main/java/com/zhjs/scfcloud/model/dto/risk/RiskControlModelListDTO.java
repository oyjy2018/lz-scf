package com.zhjs.scfcloud.model.dto.risk;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 风控模型列 入参
 */

@Data
public class RiskControlModelListDTO extends Page {

    //模型名
    private String modelName;

    //是否启用（0：否；1：是）
    private String hasEnabled;

    private Long userId;

    private Integer permissionType;

    private String permissionOrgIds;

}
