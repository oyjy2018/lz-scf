package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 数据字典详细信息表实体
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 17:40
 * @since
 */


@Data
@Accessors(chain = true)
@TableName("scf_cfg_dictionary_details")
public class DictionaryDetails {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父类key
     */
    private String parentKey;

    /**
     * 键
     */
    private String dictKey;

    /**
     * 值
     */
    private String dictValue;

    /**
     * 备注
     */
    private String remark1;

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
    private Date createTime;

    /**
     * 更新时间;更新时间.
     */
    private Date updateTime;
}
