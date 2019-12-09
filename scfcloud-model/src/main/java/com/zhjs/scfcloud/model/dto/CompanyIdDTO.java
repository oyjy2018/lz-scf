package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公司ID 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 13:40
 *
 * @author liuchanghai
 * @create 2019-05-20 13:40
 * @since
 */

@Data
public class CompanyIdDTO {

    @ApiModelProperty("公司ID")
    private Long companyId;
}
