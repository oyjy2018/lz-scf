package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-04 14:14
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_purchase_logistics")
public class LzProjectPurchaseLogistics {
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
     * 发货id
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
    private Date shipmentsDate;

    /**
     * 收货人
     * consignee
     */
    private String consignee;

    /**
     * 收货人手机号
     * consignee_phone_no
     */
    private String consigneePhoneNo;

    /**
     * 收货地址
     * consignee_address
     */
    private String consigneeAddress;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;
}