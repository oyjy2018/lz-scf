package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

/**
 * e签宝企业注册参数
 */
@Data
public class EsignOrganizeCreateDTO {
    private Long companyId; //公司id (在本系统的id)
    /**
     * 以下为e签宝注册需要的参数  optional表示非必填
     */
    public String name; // (string): 机构名称 ,
    public String regType; // (string): 企业注册类型，NORMAL（组织机构代码）、MERGE（社会信用代码）、REGCODE（工商注册号），默认MERGE ,
    public String organCode; // (string): 组织机构代码、工商注册号或者统一社会信用代码 ,

    public String userType; // (string, optional): 注册类型，0-缺省注册，1-代理人注册，2-法人注册，默认0

    public String legalArea; // (string, optional): 法定代表人归属地，0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 ,
    public String legalIdNo; // (string, optional): 法定代表人身份证号/护照号，法人注册时必填 ,
    public String legalName; // (string, optional): 法定代表人姓名，法人注册时必填 ,

    public String agentIdNo; // (string,optional): 代理人身份证号，代理人注册时必填 ,
    public String agentName; // (string,optional): 代理人姓名，代理人注册时必填 ,

    public String address; //(string, optional): 公司地址 ,
    public String email; // (string, optional): 邮箱地址 ,
    public String mobile; // (string, optional): 手机号码 ,
    public String organType; // (string, optional): 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构，默认0 ,
    public String scope; // (string, optional): 经营范围 ,
}
