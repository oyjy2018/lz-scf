package com.zhjs.scfcloud.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 公司基本信息
 * @author weijie.chen
 */
@Data
public class CompanyBasicInfoVO {
    //公司Id
    private Long companyId;
    //公司成员数
    private Integer memberCount;
    //公司在职人员数量
    private Integer memberWorkCount;
    //是否已进行京东企业认证(0:否 1:是)
    private Integer jdVerified;
    //是否已进行e签宝企业认证(0:否 1:是)
    private Integer esignVerified;
    //是否已设置收票账户(0:否 1:是)
    private Integer isReceiptAccount;
    //是否已设置收款账户(0:否 1:是)
    private Integer isRepayAccount;
    //系统版本信息
    private List<String> systemVersionList;
}
