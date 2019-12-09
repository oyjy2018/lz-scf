package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserIdDTO {

    @ApiModelProperty("公司用户ID")
    private Long companyUserId;
}
