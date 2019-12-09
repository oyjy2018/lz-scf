package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 15:09
 *
 * @author liuchanghai
 * @create 2019-05-22 15:09
 * @since
 */
@Data
public class PhoneVaildCodeDTO {

    @NotNull(message = "手机号为空")
    private String phone;

    /**
     * 短信类型（1：注册；2：修改密码）
     */
    @NotNull(message = "短信类型为空")
    private Integer smsType;
}
