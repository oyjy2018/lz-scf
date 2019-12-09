package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 编辑状态  入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-28 15:11
 *
 * @author liuchanghai
 * @create 2019-05-28 15:11
 * @since
 */

@Data
public class EditStatusDTO extends BaseIdDTO {
    private Integer status;
}
