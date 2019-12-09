package com.zhjs.scfcloud.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 查看审核日志 dto
 */

@Data
public class AuditLogListDTO extends Page {

    /**
     * 公司ID
     */
    @NotNull(message = "公司id不能为空")
    private Long companyId;

    /**
     * 业务类型ID
     */
    @NotNull(message = "业务类型id不能为空")
    private Long businessTypeId;

    /**
     * 业务ID
     */
    @NotNull(message = "业务id不能为空")
    private Long businessId;

}
