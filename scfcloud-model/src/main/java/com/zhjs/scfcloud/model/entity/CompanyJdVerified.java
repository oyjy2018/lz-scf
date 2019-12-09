package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:weijie.chen
 * @date:2019-07-15 11:24
 */
@Data
@Accessors(chain = true)
@TableName("scf_company_jd_verified")
public class CompanyJdVerified {
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
     * 是否已注册(0:否 1:是)
     * is_register
     */
    private Integer isRegister;

    /**
     * 商户号
     * jr_merchantId
     */
    private String jrMerchantid;

    /**
     * 实名状态(0:未实名 4:实名成功 5:实名失效)
     * mer_real_status
     */
    private Integer merRealStatus;

    /**
     * 合作商户平台实名申请单号
     * partner_apply_id
     */
    private String partnerApplyId;

    /**
     * 企业名称
     * blic_company_name
     */
    private String blicCompanyName;

    /**
     * 统一社会信用代码18位的字母数字组合
     * blic_uscc
     */
    private String blicUscc;

    /**
     * 省
     * blic_province
     */
    private String blicProvince;

    /**
     * 市
     * blic_city
     */
    private String blicCity;

    /**
     * 住址
     * blic_address
     */
    private String blicAddress;

    /**
     * 经营范围
     * blic_scope
     */
    private String blicScope;

    /**
     * 是否长期（0:false 1:true）
     * blic_long_term
     */
    private String blicLongTerm;

    /**
     * 有效期结束
     * blic_validity_end
     */
    private String blicValidityEnd;

    /**
     * 申请单审核状态(100:待审核 120:已驳回或者打款失败 200:已通过 300:不通过)
     * audit_status
     */
    private Integer auditStatus;

    /**
     * auditStatus为100的子状态(130:待打款 131:打款成功)
     * sub_audit_status
     */
    private Integer subAuditStatus;

    /**
     * 法人代表与委托人是否一致（true或false）
     * aupIs_LEP
     */
    private String aupisLep;

    /**
     * 法人代表证件类型
     * legal_person_cert_type
     */
    private String legalPersonCertType;

    /**
     * 法人代表证件号码
     * legal_person_cert_no
     */
    private String legalPersonCertNo;

    /**
     * 法人代表姓名
     * legal_person_name
     */
    private String legalPersonName;

    /**
     * 法人代表证件有效期始
     * legal_person_cert_begin_date
     */
    private String legalPersonCertBeginDate;

    /**
     * 法人代表证件有效期止
     * legal_person_cert_end_date
     */
    private String legalPersonCertEndDate;

    /**
     * 委托人证件类型
     * porxy_person_cert_type
     */
    private String porxyPersonCertType;

    /**
     * 委托人证件号码
     * porxy_person_cert_no
     */
    private String porxyPersonCertNo;

    /**
     * 委托人姓名
     * porxy_person_cert_name
     */
    private String porxyPersonCertName;

    /**
     * 委托人证件有效期始
     * porxy_person_cert_begin_time
     */
    private String porxyPersonCertBeginTime;

    /**
     * 委托人证件有效期至
     * porxy_person_cert_end_time
     */
    private String porxyPersonCertEndTime;
}