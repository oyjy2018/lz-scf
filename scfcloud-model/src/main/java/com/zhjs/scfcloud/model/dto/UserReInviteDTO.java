package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/5/23 13:56
 */
@Data
public class UserReInviteDTO {

    @NotBlank(message = "邮箱账号不能为空")
    private String email;

    private Long companyId;

    private String userName;
}
