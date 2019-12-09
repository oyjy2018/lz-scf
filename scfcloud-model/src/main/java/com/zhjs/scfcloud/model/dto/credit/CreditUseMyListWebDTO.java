package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.time.LocalDate;

/**
 *  我的用信列表dto web
 */
@Data
public class CreditUseMyListWebDTO extends Page {

    //公司id
    private Long companyId;

    //身份证号
    private String idCard;

    //用信人
    private String applyUserName;

    //项目名
    private String itemName;

    //用信id
    private Long id;

    //用信申请id
    private Long useApplyId;

    //用信开始日期-开始
    private String useBeginDateBegin;

    //用信开始日期-结束
    private String useBeginDateEnd;

    //应结清日期-开始
    private String shouldRefundDateBegin;

    //应结清日期-结束
    private String shouldRefundDateEnd;

}
