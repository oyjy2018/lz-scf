package com.zhjs.scfcloud.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <用户登录的参数>
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 15:09
 * @since
 */

@Data
public class UserLoginDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("登录账号")
    private String loginAccount;

    @ApiModelProperty("登录密码")
    private String password;

    public UserLoginDTO(){

    }

    public UserLoginDTO(Long userId, Long companyId){
        this.userId = userId;
        this.companyId = companyId;
    }

}
