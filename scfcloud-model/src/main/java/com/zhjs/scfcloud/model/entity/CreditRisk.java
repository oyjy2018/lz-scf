package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-10-12 16:00
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_risk")
public class CreditRisk {
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
     * 授信申请ID
     * credit_apply_id
     */
    private Long creditApplyId;

    /**
     * 是否项目实际控制人
     * risk_credit1
     */
    private Integer riskCredit1;

    /**
     * 是否签劳动合同
     * risk_credit2
     */
    private Integer riskCredit2;

    /**
     * 是否购买社保
     * risk_credit3
     */
    private Integer riskCredit3;

    /**
     * 社保是否时50万以上（含）
     * risk_credit4
     */
    private Integer riskCredit4;

    /**
     * 是否已签订《商务经理承包责任书》
     * risk_credit5
     */
    private Integer riskCredit5;

    /**
     * 是否已签订《项目内部承包经营责任书》
     * risk_credit6
     */
    private Integer riskCredit6;

    /**
     * 个人人行征信是否有不良记录
     * risk_credit7
     */
    private Integer riskCredit7;

    /**
     * 个人人行征信不良记录说明
     * risk_credit8
     */
    private String riskCredit8;

    /**
     * 个人是否有涉案、涉诉
     * risk_credit9
     */
    private Integer riskCredit9;

    /**
     * 个人涉案、涉诉说明
     * risk_credit10
     */
    private String riskCredit10;

    /**
     * 在集团有无欠款情况
     * risk_credit11
     */
    private Integer riskCredit11;

    /**
     * 在集团有无欠款情况说明
     * risk_credit12
     */
    private String riskCredit12;

    /**
     * 用信计划
     * risk_credit13
     */
    private String riskCredit13;

    /**
     * 申请时-确认并承诺以上信息如实填写,后果自负
     * risk_credit14
     */
    private Integer riskCredit14;

    /**
     * 补充资料时-确认并承诺以上信息如实填写,后果自负
     * risk_credit15
     */
    private Integer riskCredit15;

    /**
     * 配偶人行征信是否有不良记录
     * risk_credit16
     */
    private Integer riskCredit16;

    /**
     * 配偶人行征信不良记录说明
     * risk_credit17
     */
    private String riskCredit17;

    /**
     * 配偶是否有涉案、涉诉
     * risk_credit18
     */
    private Integer riskCredit18;

    /**
     * 配偶涉案、涉诉说明
     * risk_credit19
     */
    private String riskCredit19;

    /**
     * 还款计划
     * risk_credit20
     */
    private String riskCredit20;

    /**
     *
     * update_time
     */
    private Date updateTime;

    /**
     *
     * update_by
     */
    private Long updateBy;

    /**
     *
     * create_time
     */
    private Date createTime;

    /**
     *
     * create_by
     */
    private Long createBy;
}
