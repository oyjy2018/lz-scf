package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统功能展示数据实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 16:46
 *
 * @author liuchanghai
 * @create 2019-05-20 16:46
 * @since
 */

@Data
public class SystemFunctionVO {


    @ApiModelProperty("主键ID")
    private Integer id;

    @ApiModelProperty("功能名称")
    private String name;

    @ApiModelProperty("功能描述")
    private String remark;
}
