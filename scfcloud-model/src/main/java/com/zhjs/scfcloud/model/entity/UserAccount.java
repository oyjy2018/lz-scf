package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户手机号表实体
 * Version: 1.0.0
 *
 * @author liuchanghai
 * @create 2019-05-16 13:41
 * @since
 */


@Data
@Accessors(chain = true)
@TableName("scf_user_account")
public class UserAccount {

    /**
     * 主键ID;主键ID
     */
    private Long id;

    /**
     * 手机号码;手机号.
     */
    private String phone;
}
