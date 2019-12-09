package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用信审批完成列表dto
 */
@Data
public class CreditUseApplyAuditFinishListQueryDTO extends Page {

    //页面筛选条件-begin  选填
    @ApiModelProperty("用信申请ID")
    private String id;

    @ApiModelProperty("关联授信ID")
    private Long creditId;

    @ApiModelProperty("项目名称")
    private String creditItemName;

    //申请日期开始
    private String applyDateBegin;

    //申请日期结束
    private String applyDateEnd;

    //是否已用信
    private String hasUse = "0";

    private Long userId;

    private Integer permissionType;

    private String permissionOrgIds;

}
