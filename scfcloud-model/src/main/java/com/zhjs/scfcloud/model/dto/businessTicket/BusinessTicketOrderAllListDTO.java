package com.zhjs.scfcloud.model.dto.businessTicket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 所有商票订单 参数
 */
@Data
public class BusinessTicketOrderAllListDTO extends Page {

    //查询类型 （all:所有订单 going:交易中 success:交易成功）
    private String queryType;

    //订单id
    private Long id;

    /**
     * 询价方（卖方/甲方）名称
     * accepter_name
     */
    private String inquireCompanyName;

    /**
     * 报价方（买方/乙方）名称
     * accepter_name
     */
    private String quotationCompanyName;

    /**
     * 成交时间-起
     */
    private String dealStartDate;

    /**
     * 成交时间-止
     */
    private String dealEndDate;

    //用户id
    private Long userId;

    //权限类型
    private int permissionType;

    //权限机构
    private String permissionOrgIds;
}