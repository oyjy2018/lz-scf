package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-06-20 20:40
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_flow")
public class BusinessFlow {
    /**
     * 业务流程状态ID
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
     * 流程code
     * flow_code
     */
    private String flowCode;

    /**
     * 流程名称
     * flow_name
     */
    private String flowName;

    /**
     * 流程说明
     * explanation
     */
    private String explanation;

    /**
     * 流程类型(0：草稿；1：起始流程；2：结束流程-成功；3：结束流程-失败)
     * flow_type
     */
    private Integer flowType;

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

    /**
     * 
     * status
     */
    private Integer status;
}