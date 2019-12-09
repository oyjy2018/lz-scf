package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑部门(组织架构) 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:39
 *
 * @author liuchanghai
 * @create 2019-05-17 16:39
 * @since
 */

@Data
public class EditDeptDTO extends BaseIdDTO {

    @ApiModelProperty("部门(组织结构名称)")
    private String deptName;

    @ApiModelProperty("部门负责人")
    private String deptHead;

    @ApiModelProperty("简介")
    private String remark;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty("上级部门ID")
    private Long parentId;

    @ApiModelProperty("公司ID")
    private Long companyId;
}
