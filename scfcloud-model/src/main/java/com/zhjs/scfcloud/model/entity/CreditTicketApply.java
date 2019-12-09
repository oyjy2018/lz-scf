package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-08-07 10:57
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_ticket_apply")
public class CreditTicketApply {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司id
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型id
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 授信id
     * credit_id
     */
    private Long creditId;

    /**
     * 申请授信id
     * credit_apply_id
     */
    private Long creditApplyId;

    /**
     * 流程状态
     * flow_code
     */
    private String flowCode;

    /**
     * 申请人身份证号
     * apply_user_id_card
     */
    private String applyUserIdCard;

    /**
     * 申请人姓名
     * apply_user_name
     */
    private String applyUserName;

    /**
     * 授信工程项目ID
     * credit_item_id
     */
    private Long creditItemId;

    /**
     * 授信工程项目名称
     * credit_item_name
     */
    private String creditItemName;

    /**
     * 申请业务种类
     * apply_business
     */
    private String applyBusiness;

    /**
     * 申请用途
     * apply_purpose
     */
    private String applyPurpose;

    /**
     * 申请用途-其他
     * apply_purpose_other
     */
    private String applyPurposeOther;

    /**
     * 申请金额
     * apply_money
     */
    private BigDecimal applyMoney;

    /**
     * 申请期限
     * apply_deadline
     */
    private Integer applyDeadline;

    /**
     * 还款计划
     * refund_plan
     */
    private String refundPlan;

    /**
     * 收票账户名称
     * ticket_get_name
     */
    private String ticketGetName;

    /**
     * 收票银行账号
     * ticket_get_bank_account
     */
    private String ticketGetBankAccount;

    /**
     * 收票银行
     * ticket_get_bank
     */
    private Integer ticketGetBank;

    /**
     * 收票银行省
     * ticket_get_bank_province
     */
    private String ticketGetBankProvince;

    /**
     * 收票银行市
     * ticket_get_bank_city
     */
    private String ticketGetBankCity;

    /**
     * 收票银行支行
     * ticket_get_bank_branch
     */
    private String ticketGetBankBranch;

    /**
     * 处理角色ids
     * dispose_role_ids
     */
    private String disposeRoleIds;

    /**
     * 处理人ids
     * dispose_user_ids
     */
    private String disposeUserIds;

    /**
     * 是否已经用信,0:未用信,1:已用信
     * has_use
     */
    private Integer hasUse;

    /**
     * 提交申请时间
     * apply_time
     */
    private Date applyTime;

    /**
     * 更新时间
     * update_time
     */
    private Date updateTime;

    /**
     * 更新人
     * update_by
     */
    private Long updateBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 状态
     * status
     */
    private Integer status;
}