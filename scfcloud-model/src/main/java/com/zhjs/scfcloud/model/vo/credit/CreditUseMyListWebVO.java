package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.model.enums.CreditUseEnum;
import lombok.Data;

import java.time.LocalDate;

/**
 * 我的用信列表 web
 */

@Data
public class CreditUseMyListWebVO {

    private Long companyId;

    private Long id;

    //授信申请id
    private Long creditApplyId;

    //授信id
    private Long creditId;

    //用信申请id
    private Long useApplyId;

    //用信类型
    private Integer useType;

    //用信类型名（0：开商票，1：工程贷）
    private String useTypeName;

    //用信人
    private String applyUserName;

    //项目名
    private String itemName;

    //还款状态
    private Integer refundStatus;

    //还款状态名
    private String refundStatusCH;

    //用信开始日期
    private LocalDate useBeginDate;

    //应结清日期
    private LocalDate shouldRefundDate;

    //用信金额
    private String useMoney;

    public String getRefundStatusCH() {
        return CreditUseEnum.RefundStatus.getRefundStatusDesc(this.refundStatus);
    }

    public String getUseTypeName() {
        return CreditUseEnum.UseType.getUseTypeName(this.useType);
    }
}
