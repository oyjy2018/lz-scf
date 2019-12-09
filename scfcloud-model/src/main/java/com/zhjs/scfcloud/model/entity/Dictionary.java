package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统数据字典信息表实体
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 15:43
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_dictionary")
public class Dictionary {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 键
     */
    private String dictKey;

    /**
     * 值
     */
    private String dictValue;

    /**
     * 描述
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态;状态0禁用1启用
     */
    private Integer status;

    /**
     * 创建时间;创建时间.
     */
    private LocalDateTime createTime;

    /**
     * 更新时间;更新时间.
     */
    private LocalDateTime updateTime;
}
