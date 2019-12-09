package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新建部门 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:39
 *
 * @author liuchanghai
 * @create 2019-05-17 16:39
 * @since
 */

@Data
public class AddDeptDTO {

    @ApiModelProperty("部门(组织架构)名")
    private String deptName;

    @ApiModelProperty("部门等级")
    private Integer deptLevel;

    @ApiModelProperty("部门负责人")
    private String deptHead;

    @ApiModelProperty("上级部门ID")
    private Integer parentId;

    @ApiModelProperty("公司ID")
    private Integer companyId;

    @ApiModelProperty("部门简介")
    private Integer remark;
}
