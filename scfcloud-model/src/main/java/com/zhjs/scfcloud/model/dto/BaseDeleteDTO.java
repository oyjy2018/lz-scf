package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 删除通用dto
 */

@Data
public class BaseDeleteDTO {

    @NotNull(message = "id不能为空")
    private Long id;

    private Long userId;

    private String userName;

    private Long companyId;
}
