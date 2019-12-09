package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 通过id修改一个字段 dto
 */

@Data
public class BaseSingleUpdateDTO {

    //修改用户id
    private Long updateBy;

    //修改时间
    private Date updateTime;

    //id
    @NotNull(message = "id不能为空")
    private Long id;

    //要修改的字段名
    private String updateColumn;

    //修改后的值
    @NotNull(message = "修改后值不能为空")
    private String value;
}
