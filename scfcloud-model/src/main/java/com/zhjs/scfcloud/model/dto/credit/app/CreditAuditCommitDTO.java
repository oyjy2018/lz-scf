package com.zhjs.scfcloud.model.dto.credit.app;

import com.zhjs.scfcloud.model.dto.FileDTO;
import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/25 10:35
 */
@Data
public class CreditAuditCommitDTO {

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("授信申请ID")
    @NotNull(message = "授信申请ID不能为空")
    private Long creditApplyId;

    @ApiModelProperty("业务类型ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

    @ApiModelProperty("审批人ID")
    private Long auditPersonId;

    @ApiModelProperty("审批人")
    private String auditPerson;

    @ApiModelProperty("扭转前流程CODE")
    @NotNull(message = "扭转前流程CODE不能为空")
    private String beforeFlow;

    @ApiModelProperty("扭转后流程CODE")
    @NotNull(message = "扭转后流程CODE不能为空")
    private String afterFlow;

    @ApiModelProperty("处理用户组IDS")
    private String disposeRoleIds;

    @ApiModelProperty("处理人IDS")
    private String disposeUserIds;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("审批提交表单数据")
    private List<AuditFormDTO> formDataList;

    @ApiModelProperty("附件")
    private List<FileDTO> fileList;
}
