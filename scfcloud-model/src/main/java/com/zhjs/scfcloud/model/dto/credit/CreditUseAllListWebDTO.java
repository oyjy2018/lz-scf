package com.zhjs.scfcloud.model.dto.credit;

import lombok.Data;

/**
 *  所有用信列表参数 web
 */
@Data
public class CreditUseAllListWebDTO extends CreditUseMyListWebDTO {

    /**
     * 机构类型（1：个人；2：机构）
     */
    private Integer permissionType;

    /**
     * 权限机构集合（逗号分割）
     */
    private String permissionOrgIds;

    private Long userId;

    //授信id
    private Long creditId;

}
