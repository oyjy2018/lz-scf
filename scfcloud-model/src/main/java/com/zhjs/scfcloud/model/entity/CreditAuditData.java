package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-22 17:40
 */
@Data
@Accessors(chain = true)
@TableName("scf_credit_audit_data")
public class CreditAuditData {
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
     * 跟工程公司合作年限
     * ec_cooperation_year
     */
    private BigDecimal ecCooperationYear;

    /**
     * 在集团有无拖欠的款项
     * has_bloc_debt
     */
    private Integer hasBlocDebt;

    /**
     * 是否和深华签订劳动关系
     * has_sign_sh_cont
     */
    private Integer hasSignShCont;

    /**
     * 是否在深华或分公司购买社保
     * has_sh_buy_insure
     */
    private Integer hasShBuyInsure;

    /**
     * 《商务经理承包责任书》是否已经本人签字且为最新
     * has_sign_duty_book
     */
    private Integer hasSignDutyBook;

    /**
     * 该商务经理是否出现过诉讼情况
     * has_occur_lawsuit
     */
    private Integer hasOccurLawsuit;

    /**
     * 
     * status
     */
    private String status;

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