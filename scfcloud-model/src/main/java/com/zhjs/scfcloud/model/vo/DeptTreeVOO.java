package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-05 18:07
 *
 * @author liuchanghai
 * @create 2019-06-05 18:07
 * @since
 */
@Data
public class DeptTreeVOO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("下级部门")
    private List<DeptTreeVOO> childs;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("上级部门ID")
    private Long parentId;
}
