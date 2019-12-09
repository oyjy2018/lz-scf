package com.zhjs.scfcloud.util.enums;

/**
 * 公司银行账户类型
 * @author: weijie.chen
 * @date:2019/7/6 15:00
 */
public enum CompanyBankAccountTypeEnum{

    type_1("1","收票账户"),
    type_2("2","收款账户");

    private String status;
    private String desc;

    CompanyBankAccountTypeEnum(String status,String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getValue() {
        return this.status;
    }

    public String getDesc(){
        return this.desc;
    }

    public static String getDesc(String status){
        for (CompanyBankAccountTypeEnum type : CompanyBankAccountTypeEnum.values()) {
            if (type.status.equals(status)) {
                return type.desc;
            }
        }
        return null;
    }

}
