package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门下拉列表 视图数据
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-30 11:16
 *
 * @author liuchanghai
 * @create 2019-05-30 11:16
 * @since
 */

@Data
public class DeptSelectVO {

    @ApiModelProperty("部门ID")
    private Integer id;

    @ApiModelProperty("部门名称")
    private String deptName;
}
