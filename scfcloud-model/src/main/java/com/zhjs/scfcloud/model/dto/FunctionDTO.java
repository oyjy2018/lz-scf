package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 角色授权 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-31 13:16
 *
 * @author liuchanghai
 * @create 2019-05-31 13:16
 * @since
 */

@Data
public class FunctionDTO {

    private String functCode;

    // 角色类型
    private Long systemId;

}
