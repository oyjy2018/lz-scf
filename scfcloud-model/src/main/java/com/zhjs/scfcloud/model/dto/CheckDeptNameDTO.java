package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检查部门(组织架构名)名称是否存在 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 15:20
 *
 * @author liuchanghai
 * @create 2019-05-17 15:20
 * @since
 */
@Data
public class CheckDeptNameDTO {

    @ApiModelProperty("部门名")
    private String deptName;
}
