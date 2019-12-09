package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:liuchanghai
 * @date:2019-07-01 10:09
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_use")
public class CreditUse {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 公司id
     * company_id
     */
    private Long companyId;

    /**
     * 授信申请id
     * credit_apply_id
     */
    private Long creditApplyId;

    /**
     * 授信id
     * credit_id
     */
    private Long creditId;

    /**
     * 项目id
     * item_id
     */
    private Long itemId;

    /**
     * 项目名称
     * item_name
     */
    private String itemName;

    /**
     * 用信类型（0：开商票，1：工程贷）
     * use_type
     */
    private Integer useType;

    /**
     * 用信申请ID(开商票或工程贷的申请id)
     * use_apply_id
     */
    private Long useApplyId;

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
     * 用信金额
     * use_money
     */
    private BigDecimal useMoney;

    /**
     * 用信开始日期
     * use_begin_date
     */
    private Date useBeginDate;

    /**
     * 应结清日期
     * should_refund_date
     */
    private Date shouldRefundDate;

    /**
     * 还款状态
     * refund_status
     */
    private Integer refundStatus;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

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
     * 状态
     * status
     */
    private Integer status;
}