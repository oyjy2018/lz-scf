package com.zhjs.scfcloud.model.dto.credit;

import com.zhjs.scfcloud.model.entity.File;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *  授信业务保存草稿dto
 */
@Data
public class CreditApplyFileSaveDTO {

    //公司ID
    @NotNull(message = "公司id不能为空")
    private Long companyId;

//    //业务类型id
//    @NotNull(message = "业务类型id不能为空")
//    private Long businessTypeId;

    //业务id
    private Long businessId;

    /**
     * 流程扭转ID
     */
    private Long workFlowId;

    private List<File> fileList;
}
