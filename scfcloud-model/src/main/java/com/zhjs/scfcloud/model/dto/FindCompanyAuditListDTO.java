package com.zhjs.scfcloud.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FindCompanyAuditListDTO {

    /**
     *审核状态:0待审核 1已审核
     */
    private Integer status;

    /**
     * 分页数
     */
    @NotNull(message = "分页数不能为空")
    private Long current;

    /**
     * 分页大小
     */
    @NotNull(message = "分页大小不能为空")
    private Long size;

}
