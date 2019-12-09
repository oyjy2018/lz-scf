package com.zhjs.scfcloud.model.vo.credit;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreditRecordMyListAppVO {

    private Long id;

    //项目名称
    private String itemName;

    //授信金额
    private int creditAmount;

    //已用金额
    private int useCreditAmount;

    /**
     * 授信开始日期
     * credit_start
     */
    private LocalDate creditStart;

    /**
     * 授信结束日期
     * credit_end
     */
    private LocalDate creditEnd;

    private String flowCode;

    private Long businessTypeId;
}
