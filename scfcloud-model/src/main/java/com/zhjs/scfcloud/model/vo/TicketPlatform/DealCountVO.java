package com.zhjs.scfcloud.model.vo.TicketPlatform;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 成交统计VO
 * @author weijie.chen
 */
@Data
public class DealCountVO {
    /**
     * 今日成交数
     */
    private Integer dealCountDay;
    /**
     * 今日成交金额
     */
    private BigDecimal dealAmountDay;
    /**
     * 本月成交数
     */
    private Integer dealCountMonth;
    /**
     * 本月成交金额
     */
    private BigDecimal dealAmountMonth;

    /**
     * 累计成交数
     */
    private Integer dealCount;

    /**
     * 累计成交金额
     */
    private BigDecimal dealAmount;
}
