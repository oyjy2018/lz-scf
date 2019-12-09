package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-20 20:54
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_attr_val")
public class BusinessAttrVal {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 业务属性ID
     * business_attr_id
     */
    private Long businessAttrId;

    /**
     * 值key
     * value_key
     */
    private String valueKey;

    /**
     * 值中文
     * value_ch
     */
    private String valueCh;

    /**
     * 上级属性值key
     * superior_attr_key
     */
    private String superiorAttrKey;

    /**
     * 排序
     * sort
     */
    private Integer sort;

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
     * 
     * company_id
     */
    private Long companyId;

    /**
     * 
     * status
     */
    private Byte status;
}