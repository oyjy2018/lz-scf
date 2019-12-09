package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 编辑系统版本 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 11:10
 *
 * @author liuchanghai
 * @create 2019-05-23 11:10
 * @since
 */

@Data
public class EditSystemVDTO extends BaseIdDTO {

    private String systemName;

    private String systemVersion;

    private String systemDesc;

}
