package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 *  商票列表参数 web
 */
@Data
public class CreditTicketListWebDTO extends Page {

    //用信id
    private Long creditUseId;

    /**
     * 出票人公司全称
     */
    private String ticketGiveCompany;

    /**
     * 承兑人公司全称
     */
    private String acceptorCompany;

    /**
     * 票据号
     */
    private String ticketNo;

    // 出票日期-开始值
    private String ticketStartBegin ;

    // 出票日期-结束值
    private String ticketStartEnd ;

    // 到期日期-开始值
    private String ticketEndBegin;

    // 到期日期-结束值
    private String ticketEndEnd;

    private Long companyId;

    private Long userId;

   // private String idCard;

    /**
     * 机构类型（1：个人；2：机构）
     */
    private Integer permissionType;

    /**
     * 权限机构集合（逗号分割）
     */
    private String permissionOrgIds;

}
