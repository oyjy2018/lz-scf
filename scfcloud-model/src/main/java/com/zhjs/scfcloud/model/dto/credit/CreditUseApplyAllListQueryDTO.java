package com.zhjs.scfcloud.model.dto.credit;


import lombok.Data;

/**
 * 所有用信列表dto 继承我的用信列表dto
 */
@Data
public class CreditUseApplyAllListQueryDTO extends CreditUseApplyMyListQueryDTO {

    private Integer permissionType;

    private String permissionOrgIds;

    private Long companyId;

    private String roleIds;


}
