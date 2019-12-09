package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-07-26 15:49
 */
@Data
@Accessors(chain = true)
@TableName("scf_system")
public class System {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 系统名称
     * system_name
     */
    private String systemName;

    /**
     * 创建人
     * create_by
     */
    private String createBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;

    /**
     * 修改人
     * update_by
     */
    private String updateBy;

    /**
     * 修改时间
     * update_time
     */
    private Date updateTime;

    /**
     * 删除标志:0未删除;1已删除
     * is_delete
     */
    private Integer isDelete;
}