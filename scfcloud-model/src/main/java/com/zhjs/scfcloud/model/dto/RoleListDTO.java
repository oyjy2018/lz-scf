package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询角色列表入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:50
 *
 * @author liuchanghai
 * @create 2019-05-17 16:50
 * @since
 */

@Data
public class RoleListDTO {

    @ApiModelProperty("公司ID")
    private Long companyId;
}
