package com.zhjs.scfcloud.model.vo.credit;

import lombok.Data;

/**
 * @author: dailongting
 * @date:2019/6/27 14:17
 */
@Data
public class PersonalCreditVO {

    /**
     * 授信记录ID
     */
    private Long id;

    /**
     * 授信申请ID
     */
    private Long creditApplyId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 申请人名称
     */
    private String customerName;

    /**
     * 申请人身份证
     */
    private String idCard;

    /**
     * 项目ID
     */
    private Long itemId;

    /**
     * 项目名称
     */
    private String itemName;
}
