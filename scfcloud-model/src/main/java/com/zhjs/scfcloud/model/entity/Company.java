package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author:weijie.chen
 * @date:2019-07-21 16:10
 */
@Data
@Accessors(chain = true)
@TableName("scf_company")
public class Company implements Serializable {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 公司名称
     * company_name
     */
    private String companyName;

    /**
     * 营业执照URL
     * blic_url
     */
    private String blicUrl;

    /**
     * 信用代码
     * credit_code
     */
    private String creditCode;

    /**
     * 营业期限(长期或具体日期)
     * blic_end_date
     */
    private String blicEndDate;

    /**
     * 省
     * province_name
     */
    private String provinceName;

    /**
     * 市
     * city_name
     */
    private String cityName;

    /**
     * 区
     * district_name
     */
    private String districtName;

    /**
     * 详细地址
     * detail_addr
     */
    private String detailAddr;

    /**
     * 行业大类
     * company_nature
     */
    private String companyNature;

    /**
     * 具体行业
     * company_nature_concrete
     */
    private String companyNatureConcrete;

    /**
     * 联系电话
     * contact_number
     */
    private String contactNumber;

    /**
     * 公司规模
     * staff_size
     */
    private String staffSize;

    /**
     * 公司类别
     * company_category
     */
    private String companyCategory;

    /**
     * 法人身份证-国徽页
     * legal_person_cert_front_url
     */
    private String legalPersonCertFrontUrl;

    /**
     * 法人身份证-头像页
     * legal_person_cert_back_url
     */
    private String legalPersonCertBackUrl;

    /**
     * 法人代表姓名
     * legal_person_name
     */
    private String legalPersonName;

    /**
     * 法人代表证件号码
     * legal_person_cert_no
     */
    private String legalPersonCertNo;

    /**
     * 法人代表证件有效期止
     * legal_person_cert_end_date
     */
    private String legalPersonCertEndDate;

    /**
     * 法人代表与被授权人是否一致(0:否 1:是)
     * aupis_lep
     */
    private Integer aupisLep;

    /**
     * 被授权人身份证-国徽页
     * porxy_person_cert_front_url
     */
    private String porxyPersonCertFrontUrl;

    /**
     * 被授权人身份证-头像页
     * porxy_person_cert_back_url
     */
    private String porxyPersonCertBackUrl;

    /**
     * 被授权人姓名
     * porxy_person_name
     */
    private String porxyPersonName;

    /**
     * 被授权人证件号码
     * porxy_person_cert_no
     */
    private String porxyPersonCertNo;

    /**
     * 被授权人代表证件有效期止
     * porxy_person_cert_end_date
     */
    private String porxyPersonCertEndDate;

    /**
     * 授权委托书
     * porxy_commission_url
     */
    private String porxyCommissionUrl;

    /**
     * 注册用户ID
     * reg_user_id
     */
    private Long regUserId;

    /**
     * 状态（0：待激活；1：已启用；2：已禁用）
     * status
     */
    private Integer status;

    /**
     * 是否限制承兑方(0:不限制;1:限制)
     * is_acceptor_limit
     */
    private Integer isAcceptorLimit;

    /**
     * 修改人ID
     * update_by_id
     */
    private Long updateById;

    /**
     * 修改时间
     * update_time
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     * create_by_id
     */
    private Long createById;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;

    /**
     * 删除标志:0未删除;1已删除
     * is_delete
     */
    private Integer isDelete;
}