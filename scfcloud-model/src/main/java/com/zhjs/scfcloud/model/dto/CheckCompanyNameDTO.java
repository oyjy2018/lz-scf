package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检查公司名是否存在 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 14:35
 *
 * @author liuchanghai
 * @create 2019-05-17 14:35
 * @since
 */

@Data
public class CheckCompanyNameDTO {

    @ApiModelProperty("公司或组织名")
    private String companyName;
}
