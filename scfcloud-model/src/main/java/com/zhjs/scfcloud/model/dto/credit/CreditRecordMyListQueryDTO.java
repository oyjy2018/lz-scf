package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Date;

/**
 *  我的授信列表dto
 */
@Data
public class CreditRecordMyListQueryDTO extends Page {
    //授信ID
    private Long id;

    private String customerName;

    private String itemName;

    private String applyId;

    //公司ID
    private Long companyId;

    private String idCard;

    // 授信开始时间-开始值
    private String creditStartDateStart ;

    // 授信开始时间-结束值
    private String creditStartDateEnd ;

    // 授信结束时间-开始值
    private String creditEndDateStart;

    // 授信结束时间-结束值
    private String creditEndDateEnd;

    private String client = "app";

}
