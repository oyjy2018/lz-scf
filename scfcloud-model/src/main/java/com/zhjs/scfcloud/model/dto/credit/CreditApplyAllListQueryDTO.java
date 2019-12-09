package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 *  授信业务保存草稿dto
 */
@Data
public class CreditApplyAllListQueryDTO extends Page {

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

    private Long userId;

    private Long companyId;

    private String roleIds;

}
