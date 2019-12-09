package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-29 15:48
 *
 * @author liuchanghai
 * @create 2019-05-29 15:48
 * @since
 */

@Data
public class UserIdAndCompanyIdDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("公司ID")
    private Long companyId;

}
