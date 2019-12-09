package com.zhjs.scfcloud.model.dto.businessTicket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.model.annotation.IsLong;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 给我的询价列表 参数
 */
@Data
public class BusinessTicketInquireMyAssignListDTO extends Page {

    //询价单id
    @IsLong
    private String id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 承兑人名称
     * accepter_name
     */
    private String accepterName;

    /**
     * 询价方名称
     * company_name
     */
    private String companyName;


    /** 发布时间-起始值
     * create_time
     */
    private String createTimeBegin;

    /** 发布时间-结束值
     * create_time
     */
    private String createTimeEnd;

    //询价状态(1:待报价;2:竞价中;3:询价成功;4:询价已截止;5:询价已撤销;)
    private String status;

}