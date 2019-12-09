package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:oyjy
 * @date:2019-06-12 12:04
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_attr_classify")
public class BusinessAttrClassify {
    /**
     * 归类ID
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
     * 归类名称
     * classify_name
     */
    private String classifyName;

    /**
     * 排序
     * sort
     */
    private Integer sort;

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