package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-10-21 17:07
 */
@Data
@Accessors(chain = true)
@TableName("scf_platform_service_rate")
public class PlatformServiceRate {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * 系统id
     * system_id
     */
    private Long systemId;

    /**
     * 服务费百分数，
     * rate
     */
    private BigDecimal rate;

    /**
     * 删除状态(0:未删;1:已删)
     * delete_status
     */
    private Integer deleteStatus;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;
}