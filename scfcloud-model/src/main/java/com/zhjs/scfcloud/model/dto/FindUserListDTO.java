package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户列表 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-22 11:31
 *
 * @author liuchanghai
 * @create 2019-05-22 11:31
 * @since
 */

@Data
public class FindUserListDTO {

    private Long companyId;

    private Long deptId;

    private Long roleId;

    private String userName;

    private List<Integer> status;

    private Long current;

    private Long size;

}
