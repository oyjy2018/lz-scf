package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据字典展示数据实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 17:11
 *
 * @author liuchanghai
 * @create 2019-05-20 17:11
 * @since
 */

@Data
public class DictVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("键")
    private String key;

    @ApiModelProperty("值")
    private String value;

}
