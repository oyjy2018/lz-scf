package com.zhjs.scfcloud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: dailongting
 * @date:2019/5/24 14:29
 */
@Data
public class CompanyAuditBasicVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("信用代码")
    private String creditCode;

    @ApiModelProperty("法人")
    private String legalPerson;

    @ApiModelProperty("申请人")
    private String name;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("所属行业")
    private String companyNatureKey;

    @ApiModelProperty("所属具体行业")
    private String companyNatureConcreteKey;

    @ApiModelProperty("人员规模")
    private String staffSizeKey;

    @ApiModelProperty("营业执照图片url地址")
    private String businessLicenceUrl;

    @ApiModelProperty("状态")
    private Integer status;
}
