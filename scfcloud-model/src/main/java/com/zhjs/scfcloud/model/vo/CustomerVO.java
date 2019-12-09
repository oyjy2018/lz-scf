package com.zhjs.scfcloud.model.vo;

import lombok.Data;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 16:35
 *
 * @author liuchanghai
 * @create 2019-06-11 16:35
 * @since
 */

@Data
public class CustomerVO {

    /**
     * 客户姓名.
     */
    private String customerName;

    /**
     * 客户身份证.
     */
    private String idCard;

    /**
     * 客户性别.
     */
    private String gender;

    /**
     * 客户年龄.
     */
    private Integer age;

    /**
     * 客户手机号.
     */
    private String mobile;

    /**
     * 客户婚姻状态.
     */
    private String maritalStatus;

    /**
     * 配偶姓名.
     */
    private String spouseName;

    /**
     * 配偶身份证.
     */
    private String spouseIdCard;

    /**
     * 配偶手机号码.
     */
    private String spouseMobile;

    /**
     * 家庭住址省CODE.
     */
    private String familyAddressProvinceCode;

    /**
     * 家庭住址省.
     */
    private String familyAddressProvince;

    /**
     * 家庭住址市CODE.
     */
    private String familyAddressCityCode;

    /**
     * 家庭住址市.
     */
    private String familyAddressCity;

    /**
     * 家庭住址区/县CODE.
     */
    private String familyAddressRegionCode;

    /**
     * 家庭住址区/县.
     */
    private String familyAddressRegion;

    /**
     * 家庭住址.
     */
    private String familyAddress;

    /**
     * 家庭住址房产状态.
     */
    private String housingOwnership;
}
