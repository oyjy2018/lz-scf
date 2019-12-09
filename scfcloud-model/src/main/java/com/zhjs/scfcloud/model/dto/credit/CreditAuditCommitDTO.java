package com.zhjs.scfcloud.model.dto.credit;

import com.zhjs.scfcloud.model.dto.FileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 授信审核提交 入参
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-18 17:34
 *
 * @author liuchanghai
 * @create 2019-06-18 17:34
 * @since
 */

@Data
public class CreditAuditCommitDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("公司ID")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    @ApiModelProperty("授信申请ID")
    @NotNull(message = "授信申请ID不能为空")
    private Long creditApplyId;

    @ApiModelProperty("授信申请ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

    @ApiModelProperty("审批人ID")
    private Long auditPersonId;

    @ApiModelProperty("审批人")
    private String auditPerson;

    @ApiModelProperty("流程扭转ID")
    private Long flowId;

    @ApiModelProperty("扭转前流程CODE")
    private String beforeFlow;

    @ApiModelProperty("扭转后流程CODE")
    private String afterFlow;

    @ApiModelProperty("处理用户组IDS")
    private String disposeRoleIds;

    @ApiModelProperty("处理人IDS")
    private String disposeUserIds;

    @ApiModelProperty("上传或删除文件说明")
    private String uploadDeleteFileDesc;

    @ApiModelProperty("在状态")
    private String statusDesc;

    @ApiModelProperty("评论内容")
    @Size(max = 250,message = "评论内容不能超过250字")
    private String commentContent;

    @ApiModelProperty("流程扭转说明")
    private String flowDesc;

    @ApiModelProperty("审批提交表单数据")
    private List<AuditFormDTO> formDataList;

    @ApiModelProperty("附件")
    private List<FileDTO> fileList;

}
