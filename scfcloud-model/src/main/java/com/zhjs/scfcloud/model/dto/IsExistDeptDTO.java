package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门(组织架构)是否存在入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:39
 *
 * @author liuchanghai
 * @create 2019-05-17 16:39
 * @since
 */

@Data
public class IsExistDeptDTO {

    @ApiModelProperty("公司ID")
    private Integer companyId;

    @ApiModelProperty("部门(组织架构)名称")
    private String deptName;
}
