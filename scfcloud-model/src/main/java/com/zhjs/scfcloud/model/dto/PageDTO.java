package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 分页 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-29 13:10
 *
 * @author liuchanghai
 * @create 2019-05-29 13:10
 * @since
 */

@Data
public class PageDTO {

    private Long current;
    private Long size;
}
