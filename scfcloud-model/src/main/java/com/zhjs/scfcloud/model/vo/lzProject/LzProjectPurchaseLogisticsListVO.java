package com.zhjs.scfcloud.model.vo.lzProject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-04 14:14
 */
@Data
public class LzProjectPurchaseLogisticsListVO {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 采购合同号
     * purchase_contract_no
     */
    private String purchaseContractNo;

    /**
     * 发货物流编号
     * shipments_no
     */
    private String shipmentsNo;

    /**
     * 发货人
     * shipper
     */
    private String shipper;

    /**
     * 发货时间
     * shipments_date
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date shipmentsDate;

    /**
     * 收货人
     * consignee
     */
    private String consignee;

    /**
     * 收货地址
     * consignee_address
     */
    private String consigneeAddress;
}