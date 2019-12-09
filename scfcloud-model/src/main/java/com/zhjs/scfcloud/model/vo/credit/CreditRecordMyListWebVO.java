package com.zhjs.scfcloud.model.vo.credit;

import com.alibaba.fastjson.annotation.JSONField;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreditRecordMyListWebVO {

    private Long id;

    //项目名称
    private String itemName;

    //授信金额
    private String creditAmount;

    //客户
    private String customerName;

    /**
     * 授信开始日期
     * credit_start
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date creditStart;

    /**
     * 授信结束日期
     * credit_end
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date creditEnd;

    //申请id
    private String applyId;

    private String flowCode;

    private Long companyId;

    private Long businessTypeId;

    //是否人工导入 0：否 1：是
    private String ifImport;


    //已用授信金额
    private String usedCreditAmount;

    //剩余授信金额
    private String remainCreditAmount;

    //授信期限
    private int creditDays;

    public String getRemainCreditAmount() {
        usedCreditAmount = StringUtil.isEmpty(usedCreditAmount) ? "0" :usedCreditAmount;
        return (new BigDecimal(getCreditAmount()).subtract(new BigDecimal(usedCreditAmount))).toString();
    }

    public int getCreditDays() {
        return DateUtil.getDateDistance(getCreditStart(),getCreditEnd());
    }
}
