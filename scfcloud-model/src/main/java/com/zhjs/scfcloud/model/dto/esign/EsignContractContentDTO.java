package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 合同内容DTO
 */
@Data
public class EsignContractContentDTO {
    public Long orderId; //订单id
    public BigDecimal annualRate; //年化率
    public BigDecimal amounts; // 交易金额 ,

    public String aName; //甲方（转让方）
    public String aLegalPerson;//甲方法人
    public String aAddress; //甲方地址 ,
    public String aContactNumber; //甲方联系方式 ,
//    public String aMoneyBankAccountName; //甲方收款账户名,
//    public String aMoneyBankAccountNo; //甲方收款账号,
//    public String aMoneyBankChildName; //甲方收款账号开户行,

    public String bName; //乙方（受让方）
    public String bLegalPerson;//乙方法人
    public String bAddress; //乙方地址 ,
    public String bContactNumber; //甲方联系方式 ,
//    public String bTicketBankAccountName; //乙方收票账户名,
//    public String bTicketBankAccountNo; //乙方收票账号,
//    public String bTicketBankChildName; //乙方收票账号开户行,


    public BigDecimal getAnnualRate() {
        if (annualRate != null && new BigDecimal(0).compareTo(annualRate) == 0) {
            return new BigDecimal(0);
        }
        return annualRate;
    }
}
