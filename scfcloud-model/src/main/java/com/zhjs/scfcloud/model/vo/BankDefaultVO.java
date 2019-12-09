package com.zhjs.scfcloud.model.vo;

import lombok.Data;

@Data
public class BankDefaultVO {
    //是否已设置收票账户(0:否 1:是)
    private Integer isReceiptAccount;
    //是否已设置收款账户(0:否 1:是)
    private Integer isRepayAccount;
}
