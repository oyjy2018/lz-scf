package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 验证码校验 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 15:14
 *
 * @author liuchanghai
 * @create 2019-05-22 15:14
 * @since
 */

@Data
public class CheckPhoneVCodeDTO {

    private String phone;
    private String vcode;
}
