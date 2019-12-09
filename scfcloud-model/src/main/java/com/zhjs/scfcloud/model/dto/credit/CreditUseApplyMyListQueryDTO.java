package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的用信列表dto
 */
@Data
public class CreditUseApplyMyListQueryDTO extends Page {

    //页面筛选条件-begin  选填
    @ApiModelProperty("用信申请ID")
    private String id;

    @ApiModelProperty("申请人")
    private String applyUserName;

    @ApiModelProperty("关联授信ID")
    private Long creditId;

    @ApiModelProperty("关联授信申请ID")
    private Long creditApplyId;

    @ApiModelProperty("项目名称")
    private String creditItemName;

    @ApiModelProperty("流程状态")
    private String flowCode;
    //页面筛选条件-begin

    //取登录用户id
    @ApiModelProperty("用户ID")
    private Long userId;


}
