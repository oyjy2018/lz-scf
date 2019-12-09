package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:oyjy
 * @date:2019-06-13 16:08
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_fee_formula")
public class BusinessFeeFormula {
    /**
     * 业务费用计算公式ID
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
     * 业务属性字段ID
     * business_attr_id
     */
    private Long businessAttrId;

    /**
     * 费用计算公式
     * formula
     */
    private String formula;

    /**
     * 保留的小数位
     * digits
     */
    private Integer digits;

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

    /**
     * 
     * status
     */
    private Byte status;
}