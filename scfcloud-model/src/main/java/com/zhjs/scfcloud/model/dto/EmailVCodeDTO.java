package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-15 11:20
 *
 * @author liuchanghai
 * @create 2019-06-15 11:20
 * @since
 */
@Data
public class EmailVCodeDTO {

    private String email;

    private String vcode;

    private String userName;
}
