package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * 查询部门列表 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-17 16:39
 *
 * @author liuchanghai
 * @create 2019-05-17 16:39
 * @since
 */

@Data
public class DeptListDTO extends CompanyIdDTO {
        private Long userId;
}
