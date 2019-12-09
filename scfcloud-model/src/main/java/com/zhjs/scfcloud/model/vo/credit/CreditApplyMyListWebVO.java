package com.zhjs.scfcloud.model.vo.credit;

import lombok.Data;

@Data
public class CreditApplyMyListWebVO extends CreditApplyMyListAppVO{

    //申请人
    protected String customerName;

    //申请额度
    protected String applyCreditMoney;

    //审批额度
    protected String auditCreditMoney;

    //审批授信开始日
    protected String auditCreditBeginDate;

    //审批授信截止日
    protected String auditCreditEndDate;

    //公司id
    protected Long companyId;
}
