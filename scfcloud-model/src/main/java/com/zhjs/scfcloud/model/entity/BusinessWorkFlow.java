package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-15 14:33
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_work_flow")
public class BusinessWorkFlow {
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
     * 扭转前流程CODE
     * before_flow
     */
    private String beforeFlow;

    /**
     * 扭转后流程CODE
     * after_flow
     */
    private String afterFlow;

    /**
     * 是否可编辑
     * has_edit
     */
    private Integer hasEdit;

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