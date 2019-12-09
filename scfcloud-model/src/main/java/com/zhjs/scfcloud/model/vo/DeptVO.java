package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门展示数据实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 14:25
 *
 * @author liuchanghai
 * @create 2019-05-20 14:25
 * @since
 */

@Data
public class DeptVO {

    @ApiModelProperty("部门ID")
    private Long id;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门等级")
    private Integer deptLevel;

    @ApiModelProperty("部门负责人")
    private String deptHead;

    @ApiModelProperty("上级部门ID")
    private Long parentId;
}
