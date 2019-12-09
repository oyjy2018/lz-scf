package com.zhjs.scfcloud.model.dto.ticket;

import com.zhjs.scfcloud.model.entity.File;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: dailongting
 * @date:2019/7/1 18:16
 */
@Data
public class CreditTicketFileSaveDTO {
    //公司ID
    @NotNull(message = "公司id不能为空")
    private Long companyId;

    //业务类型id
//    @NotNull(message = "业务类型id不能为空")
//    private Long businessTypeId;

    //业务id
    private Long businessId;

    /**
     * 流程扭转ID
     */
    @NotNull(message = "流程扭转ID不能为空")
    private Long workFlowId;

    /**
     * 文件对象
     */
    private File file;

    private Long userId;

    /**
     * 当前流程code
     */
    @NotNull(message = "当前流程code不能为空")
    private String flowCode;
}
