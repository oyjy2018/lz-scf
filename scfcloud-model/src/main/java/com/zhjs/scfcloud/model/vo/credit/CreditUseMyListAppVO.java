package com.zhjs.scfcloud.model.vo.credit;

import com.zhjs.scfcloud.model.enums.CreditUseEnum;
import lombok.Data;
import java.time.LocalDate;

/**
 * 我的用信列表 app
 */

@Data
public class CreditUseMyListAppVO {

    private Integer id;

    private String itemName;

    private Integer refundStatus;

    private String refundStatusCH;

    private LocalDate shouldRefundDate;

    private String shouldRefundMoney;

    public String getRefundStatusCH() {
        return CreditUseEnum.RefundStatus.getRefundStatusDesc(this.refundStatus);
    }
}
