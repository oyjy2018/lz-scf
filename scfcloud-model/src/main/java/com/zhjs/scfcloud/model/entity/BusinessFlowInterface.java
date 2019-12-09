package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-11 17:07
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_flow_interface")
public class BusinessFlowInterface {
    /**
     * 业务流程对接接口ID
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
     * 业务流程ID
     * work_flow_id
     */
    private Long workFlowId;

    /**
     * 接口地址
     * url
     */
    private String url;

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