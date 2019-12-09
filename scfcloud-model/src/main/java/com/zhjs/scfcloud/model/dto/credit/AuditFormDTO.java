package com.zhjs.scfcloud.model.dto.credit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-21 17:11
 *
 * @author liuchanghai
 * @create 2019-06-21 17:11
 * @since
 */

@Data
public class AuditFormDTO {

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("字段属性名")
    private String columnName;

    @ApiModelProperty("字段中文名")
    private String columnCh;

    @ApiModelProperty("值")
    private String columnValue;

    @ApiModelProperty("项目ID")
    private Long projectId;
}
