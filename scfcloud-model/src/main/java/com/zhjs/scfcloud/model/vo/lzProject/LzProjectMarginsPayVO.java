package com.zhjs.scfcloud.model.vo.lzProject;

import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: dailongting
 * @date:2019/11/22 9:50
 */
@Data
public class LzProjectMarginsPayVO {
    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 保证金编号
     * margins_no
     */
    private String marginsNo;

    /**
     * 缴纳日期
     * pay_date
     */
    private String payDate;

    /**
     * 缴纳金额
     * pay_money
     */
    private BigDecimal payMoney;

    /**
     * 保证金类型
     * margins_type
     */
    private String marginsType;

    /**
     * 保证金状态（已缴纳、付甲方、已提取）
     * margins_status
     */
    private String marginsStatus;

    public void setPayDate(Date payDate) {
        this.payDate = DateUtil.format(payDate);
    }
}
