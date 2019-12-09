package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 公司与金融产品信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-21 18:42
 *
 * @author liuchanghai
 * @create 2019-05-21 18:42
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_cfg_company_product")
public class CompanyProduct {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 产品ID.
     */
    private Long productId;

    /**
     * 公司ID.
     */
    private Long companyId;


    /**
     * 状态.
     */
    private Integer status;
}
