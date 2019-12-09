package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 地址信息表实体
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-23 14:03
 *
 * @author liuchanghai
 * @create 2019-05-23 14:03
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_address")
public class Address {

    private String id;

    private String name;

    private String parentId;
}
