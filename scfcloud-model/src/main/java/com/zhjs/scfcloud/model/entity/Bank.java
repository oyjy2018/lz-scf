package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 银行信息表实体类
 * <功能详细描述>
 * Version: 1.0.0, 2019-05-27 15:06
 *
 * @author liuchanghai
 * @create 2019-05-27 15:06
 * @since
 */

@Data
@Accessors(chain = true)
@TableName("scf_bank")
public class Bank {

    /**
     * 主键ID.
     */
    private Long id;

    /**
     * 银行中文名.
     */
    private String bankNameCn;

    /**
     * 银行英文名.
     */
    private String bankNameEn;

    /**
     * 银行许可证编号.
     */
    private String bankIcpNo;

    /**
     * 银行类型.
     */
    private String bankType;
}
