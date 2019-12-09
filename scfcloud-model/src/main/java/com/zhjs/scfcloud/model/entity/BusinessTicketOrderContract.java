package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-08-30 10:19
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_order_contract")
public class BusinessTicketOrderContract {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 订单id
     * order_id
     */
    private Long orderId;

    /**
     * 合同类型（0：系统生成，1：用户上传）
     * contract_type
     */
    private Integer contractType;

    /**
     * 合同文件在领筑互联系统生成的key
     * contract_file_key
     */
    private String contractFileKey;

    /**
     * 合同文件在本系统的保存地址
     * contract_file_url
     */
    private String contractFileUrl;

    /**
     * 是否当前启用（0：否，1：是）
     * is_use
     */
    private Integer isUse;

    /**
     * 删除状态（0：未删，1：已删）
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