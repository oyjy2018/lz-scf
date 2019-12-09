package com.zhjs.scfcloud.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**

 * @author weijie.chen
 * @since
 */

@Data
public class CompanyUserInfoDTO {

    @ApiModelProperty("所属公司")
    private String companyName;

    @ApiModelProperty("用户组")
    private String roleName ;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("真实姓名")
    private String userName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机")
    private String phone;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
