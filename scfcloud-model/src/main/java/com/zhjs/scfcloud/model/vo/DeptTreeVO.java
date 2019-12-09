package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-28 09:07
 *
 * @author liuchanghai
 * @create 2019-05-28 09:07
 * @since
 */

@Data
public class DeptTreeVO {

    @ApiModelProperty("公司ID")
    private Long id;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("部门")
    List<DeptVO> depts;
}
