package com.zhjs.scfcloud.model.dto.esign;

import lombok.Data;

/**
 * e签宝个人注册参数  optional表示非必填
 */
@Data
public class EsignPersonCreateDTO {
    private Long userId; //公司id (在本系统的id)

    public String idNo; // (string): 身份证号/护照号 ,
    public String mobile; // (string): 手机号码 ,
    public String name; // (string): 姓名 ,
    public String personArea; // (string): 个人归属地：0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0 ,

    public String address; // (string, optional): 常用地址 ,
    public String email; // (string, optional): 邮箱地址 ,E ,
    public String organ; // (string, optional): 所属公司 ,
    public String title; // (string, optional): 职位
}
