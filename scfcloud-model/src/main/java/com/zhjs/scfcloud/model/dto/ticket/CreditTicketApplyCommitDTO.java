package com.zhjs.scfcloud.model.dto.ticket;

import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import com.zhjs.scfcloud.model.entity.File;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/7/2 14:36
 */
@Data
public class CreditTicketApplyCommitDTO {

    //业务id
    @NotNull(message = "业务id不能为空")
    private Long businessId;

    /**
     * 流程扭转ID
     */
    @NotNull(message = "流程扭转ID不能为空")
    private Long workFlowId;

    /**
     * 操作人
     */
    private Long userId;

    /**
     * 操作人名称
     */
    private String userName;
}
