package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 公司与用户关联关系信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-20 14:01
 *
 * @author liuchanghai
 * @create 2019-05-20 14:01
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_company_user")
public class CompanyUser {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 公司ID.
     */
    private Long companyId;

    /**
     * 用户ID.
     */
    private Long userId;

    /**
     * 用户在公司状态.
     */
    private Integer status;

    /**
     * 用户离职时间.
     */

    private LocalDateTime leaveTime;

    /**
     * 创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 更新时间.
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志:0未删除;1已删除
     * is_delete
     */
    private Integer isDelete;
}
