package com.zhjs.scfcloud.model.dto;

import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/25 10:35
 */
@Data
public class CommonAuditCommitDTO {

    @ApiModelProperty("公司ID")
    private Long companyId;


    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("业务ID")
    @NotNull(message = "业务ID不能为空")
    private Long businessId;

    @ApiModelProperty("业务ID对应数据库字段名")
    private String businessIdColumnName;

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
    @Size(max = 500,message = "评论内容不能超过250字")
    private String commentContent;

    @ApiModelProperty("审批提交表单数据")
    private List<AuditFormDTO> formDataList;

    @ApiModelProperty("附件")
    private List<FileDTO> fileList;
}
