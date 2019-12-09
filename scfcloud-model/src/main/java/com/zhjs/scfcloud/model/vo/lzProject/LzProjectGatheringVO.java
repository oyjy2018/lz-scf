package com.zhjs.scfcloud.model.vo.lzProject;

import com.zhjs.scfcloud.util.util.BigDecimalUtil;
import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: dailongting
 * @date:2019/10/30 10:35
 */
@Data
public class LzProjectGatheringVO {
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
    private String gatheringDate;

    /**
     * 收款类型（1：商务经理打入资金；2：甲方回款）
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
    private String gatheringMoney;

    public void setGatheringDate(Date gatheringDate) {
        this.gatheringDate = DateUtil.format(gatheringDate, "yyyy-MM-dd");
    }

    public void setGatheringMoney(BigDecimal gatheringMoney) {
        this.gatheringMoney = BigDecimalUtil.orElse(gatheringMoney).toString();
    }
}
