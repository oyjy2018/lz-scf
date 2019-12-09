package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 新建系统版本 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:09
 *
 * @author liuchanghai
 * @create 2019-05-23 11:09
 * @since
 */

@Data
public class AddSystemVDTO {

    private String systemName;

    private String systemVersion;

}
