package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author:dailongting
 * @date:2019-07-22 09:13
 */
@Data
@Accessors(chain = true)
@TableName("scf_company_system_version")
public class CompanySystemVersion {
    /**
     * 主键ID
     * ID
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 系统版本ID
     * system_version_id
     */
    private Long systemVersionId;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     * update_time
     */
    private LocalDateTime updateTime;
}