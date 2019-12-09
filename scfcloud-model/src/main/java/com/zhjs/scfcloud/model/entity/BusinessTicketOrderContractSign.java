package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-27 17:25
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_order_contract_sign")
public class BusinessTicketOrderContractSign {
    /**
     * 
     * id
     */
    private Integer id;

    /**
     * 关联订单表id
     * order_id
     */
    private Long orderId;

    /**
     * 签署方(0:甲方,1:乙方)
     * sign_party
     */
    private Integer signParty;

    /**
     * 签署金融云账户类型（0:公司,1:个人）
     * scf_account_type
     */
    private Integer scfAccountType;

    /**
     * 签署金融云账户id
     * scf_account_id
     */
    private Long scfAccountId;

    /**
     * 对应e签宝账户id
     * e_sign_account_id
     */
    private String eSignAccountId;

    /**
     * 签署时验证手机号
     * mobile
     */
    private String mobile;

    /**
     * 签署id
     * sign_service_id
     */
    private String signServiceId;

    /**
     * 签署详情链接
     * sign_detail_url
     */
    private String signDetailUrl;

    /**
     * 是否软删(0:未删,1:已删)
     * is_del
     */
    private Byte isDel;

    /**
     * 
     * update_by
     */
    private Long updateBy;

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
     * update_time
     */
    private Date updateTime;
}