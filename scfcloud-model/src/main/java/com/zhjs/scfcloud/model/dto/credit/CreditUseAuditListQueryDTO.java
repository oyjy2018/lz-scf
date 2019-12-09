package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

/**
 * 用信审批列表dto  继承我的用信列表dto
 */

@Data
public class CreditUseAuditListQueryDTO extends CreditUseApplyMyListQueryDTO {

    private Long companyId;

    private String roleIds;

    private Integer permissionType;

    private String permissionOrgIds;

}
