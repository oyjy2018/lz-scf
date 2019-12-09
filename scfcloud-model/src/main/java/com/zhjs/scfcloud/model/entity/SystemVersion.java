package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-07-23 11:55
 */
@Data
@Accessors(chain = true)
@TableName("scf_system_version")
public class SystemVersion {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 系统ID
     * system_id
     */
    private Long systemId;

    /**
     * 版本名称
     * version_name
     */
    private String versionName;

    /**
     * 系统版本号
     * version_no
     */
    private String versionNo;

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

    /**
     * 删除标志:0未删除;1已删除
     * is_delete
     */
    private Integer isDelete;
}