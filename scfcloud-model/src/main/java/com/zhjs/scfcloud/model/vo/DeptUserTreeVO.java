package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeptUserTreeVO {
    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("子部门集合")
    private List<DeptUserTreeVO> childrenList;

    @ApiModelProperty("部门用户集合")
    private List<DepaetmentUserVO> userList;

}
