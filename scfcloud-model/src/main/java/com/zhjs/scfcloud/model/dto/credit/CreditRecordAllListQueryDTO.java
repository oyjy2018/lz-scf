package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Date;

/**
 *  我的授信列表dto
 */
@Data
public class CreditRecordAllListQueryDTO extends Page {

    //授信ID
    private Long id;

    private String customerName;

    private String itemName;

    private String applyId;

    // 授信开始时间-开始值
    private String creditStartDateStart ;

    // 授信开始时间-结束值
    private String creditStartDateEnd ;

    // 授信结束时间-开始值
    private String creditEndDateStart;

    // 授信结束时间-结束值
    private String creditEndDateEnd;

    //公司ID
    private Long companyId;

    private String idCard;

    /**
     * 机构类型（1：个人；2：机构）
     */
    private Integer permissionType;

    /**
     * 权限机构集合（逗号分割）
     */
    private String permissionOrgIds;


}
