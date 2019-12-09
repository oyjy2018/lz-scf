package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-11 17:10
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_type")
public class BusinessType {
    /**
     * 业务ID
     * id
     */
    private Long id;

    /**
     * 所属公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 业务名称
     * business_name
     */
    private String businessName;

    /**
     * 业务描述
     * remark
     */
    private String remark;

    /**
     * 是否平台业务（1：是；0否）
     * has_platform
     */
    private Integer hasPlatform;

    /**
     * 状态（1：启用；0禁用）
     * status
     */
    private Integer status;

    /**
     * 修改人ID
     * update_by_id
     */
    private Long updateById;

    /**
     * 创建人ID
     * create_by_id
     */
    private Long createById;

    /**
     * 修改时间
     * update_time
     */
    private Date updateTime;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
}