package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author:dailongting
 * @date:2019-08-02 09:37
 */
@Data
@Accessors(chain = true)
@TableName("scf_company_audit")
public class CompanyAudit {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

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
     * 被授权人手机号(系统账号)
     * porxy_person_phone
     */
    private String porxyPersonPhone;

    /**
     * 被授权人邮箱(默认系统账号)
     * porxy_person_email
     */
    private String porxyPersonEmail;

    /**
     * 系统账户用户ID
     * user_id
     */
    private Long userId;

    /**
     * 注册系统ID列表;使用,号隔开(英文逗号)
     * system_id_list
     */
    private String systemIdList;

    /**
     * 业务流程;使用,号隔开(英文逗号)
     * flow_list
     */
    private String flowList;

    /**
     * 审核状态:0待审核1审核通过2审核不通过
     * status
     */
    private Integer status;

    /**
     * 审核时间
     * audit_time
     */
    private LocalDateTime auditTime;

    /**
     * 审核备注
     * remark
     */
    private String remark;

    /**
     * 审核人ID
     * audit_user_id
     */
    private Long auditUserId;

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
}