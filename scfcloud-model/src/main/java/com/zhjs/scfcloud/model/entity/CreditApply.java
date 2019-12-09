package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-19 19:52
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_apply")
public class CreditApply {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 流程CODE
     * flow_code
     */
    private String flowCode;

    /**
     * 申请来源
     * apply_source
     */
    private String applySource;

    /**
     * 申请时间
     * apply_time
     */
    private Date applyTime;

    /**
     * 跟工程公司开始合作时间
     * ec_cooperation_time
     */
    private Date ecCooperationTime;

    /**
     * 跟工程公司合作年限
     * ec_cooperation_year
     */
    private BigDecimal ecCooperationYear;

    /**
     * 所属大区
     * belongs_region
     */
    private String belongsRegion;

    /**
     * 所属分公司
     * their_filiale
     */
    private String theirFiliale;

    /**
     * 大区经理
     * region_manager
     */
    private String regionManager;

    /**
     * 客户姓名
     * customer_name
     */
    private String customerName;

    /**
     * 客户身份证
     * id_card
     */
    private String idCard;

    /**
     * 客户性别
     * gender
     */
    private String gender;

    /**
     * 客户年龄
     * age
     */
    private Integer age;

    /**
     * 客户手机号
     * mobile
     */
    private String mobile;

    /**
     * 客户婚姻状态
     * marital_status
     */
    private Integer maritalStatus;

    /**
     * 配偶姓名
     * spouse_name
     */
    private String spouseName;

    /**
     * 配偶身份证
     * spouse_id_card
     */
    private String spouseIdCard;

    /**
     * 配偶手机号码
     * spouse_mobile
     */
    private String spouseMobile;

    /**
     * 家庭住址省CODE
     * family_address_province_code
     */
    private String familyAddressProvinceCode;

    /**
     * 家庭住址市CODE
     * family_address_city_code
     */
    private String familyAddressCityCode;

    /**
     * 家庭住址区/县CODE
     * family_address_region_code
     */
    private String familyAddressRegionCode;

    /**
     * 家庭住址
     * family_address
     */
    private String familyAddress;

    /**
     * 家庭住址房产状态
     * housing_ownership
     */
    private String housingOwnership;

    /**
     * 处理用户组IDS
     * dispose_role_ids
     */
    private String disposeRoleIds;

    /**
     * 处理人IDS
     * dispose_user_ids
     */
    private String disposeUserIds;

    /**
     * 更新时间
     * update_time
     */
    private Date updateTime;

    /**
     * 更新人
     * update_by
     */
    private Long updateBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;
}