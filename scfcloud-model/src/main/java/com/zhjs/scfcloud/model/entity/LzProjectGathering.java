package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-30 10:20
 */
@Data
@Accessors(chain = true)
@TableName("lz_project_gathering")
public class LzProjectGathering {
    /**
     *
     * id
     */
    private Long id;

    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 收款编号
     * gathering_no
     */
    private String gatheringNo;

    /**
     * 收款日期
     * gathering_date
     */
    private Date gatheringDate;

    /**
     * 收款类型（商务经理打入资金,甲方回款）
     * gathering_type
     */
    private String gatheringType;

    /**
     * 付款方全称
     * payer_full_name
     */
    private String payerFullName;

    /**
     * 收款方全称
     * gathering_full_name
     */
    private String gatheringFullName;

    /**
     * 收款金额
     * gathering_money
     */
    private BigDecimal gatheringMoney;

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
