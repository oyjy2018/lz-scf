package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 金融产品信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 18:40
 *
 * @author liuchanghai
 * @create 2019-05-21 18:40
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_product")
public class Product {

    /**
     * 金融产品ID.
     */
    private Long id;

    /**
     * 产品名称.
     */
    private String productName;

    /**
     * 产品状态.
     */
    private Integer status;
}
