package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  我的授信申请列表dto
 */
@Data
public class CreditApplyAuditListQueryDTO extends Page {


    private Long userId;

    private Long companyId;

    private String roleIds;

    //流程code
    private String flowCode;

    //客户姓名
    private String customerName;

    private Long businessId;

    private String itemName;

    /**
     * 机构类型（1：个人；2：机构）
     */
    private Integer permissionType;

    /**
     * 权限机构集合（逗号分割）
     */
    private String permissionOrgIds;

}
