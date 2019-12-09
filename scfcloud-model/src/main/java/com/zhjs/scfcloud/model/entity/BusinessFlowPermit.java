package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:oyjy
 * @date:2019-06-13 11:32
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_flow_permit")
public class BusinessFlowPermit {
    /**
     * 业务流程权限配置ID
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
     * 业务流程扭转ID
     * work_flow_id
     */
    private Long workFlowId;

    /**
     * 用户组ID集合
     * role_ids
     */
    private String roleIds;

    /**
     * 用户ID集合
     * user_ids
     */
    private String userIds;

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