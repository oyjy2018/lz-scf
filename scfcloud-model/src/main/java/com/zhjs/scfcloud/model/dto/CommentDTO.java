package com.zhjs.scfcloud.model.dto;

import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 16:52
 *
 * @author liuchanghai
 * @create 2019-06-22 16:52
 * @since
 */
@Data
public class CommentDTO {

    private Long companyId;

    private Long businessId;

    private Long businessTypeId;

    private String flowCode;

    public CommentDTO(Long companyId, Long businessTypeId, Long businessId){
        this.companyId = companyId;
        this.businessTypeId = businessTypeId;
        this.businessId = businessId;
    }
}
