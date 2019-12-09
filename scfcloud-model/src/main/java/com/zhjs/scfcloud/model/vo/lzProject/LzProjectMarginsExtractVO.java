package com.zhjs.scfcloud.model.vo.lzProject;

import com.zhjs.scfcloud.util.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: dailongting
 * @date:2019/11/22 9:58
 */
@Data
public class LzProjectMarginsExtractVO {
    /**
     * 项目ID（项目合同号）
     * contract_no
     */
    private String contractNo;

    /**
     * 提取保证金编号
     * extract_margins_no
     */
    private String extractMarginsNo;

    /**
     * 对应的缴纳保证金编号
     * margins_no
     */
    private String marginsNo;

    /**
     * 缴纳日期
     * extract_date
     */
    private String extractDate;

    /**
     * 缴纳金额
     * extract_money
     */
    private BigDecimal extractMoney;

    public void setExtractDate(Date extractDate) {
        this.extractDate = DateUtil.format(extractDate);
    }
}
