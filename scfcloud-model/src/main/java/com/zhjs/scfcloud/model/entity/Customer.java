package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 14:49
 *
 * @author liuchanghai
 * @create 2019-06-11 14:49
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_customer")
public class Customer {

    /**
     * 主键ID.
     */
    private Long id;

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

    /**
     * 更新人.
     */
    private Long updateBy;

    /**
     * 创建人.
     */
    private Long createBy;

    /**
     * 更新时间.
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 删除状态（0:未删除;1:已删除）
     */
    @TableLogic
    private Integer isDelete;

}
