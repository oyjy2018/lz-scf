package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author:dailongting
 * @date:2019-08-02 15:35
 */
@Data
@Accessors(chain = true)
@TableName("scf_vcode_vaild")
public class VCodeValid {
    /**
     * 
     * id
     */
    private Integer id;

    /**
     * code的token
     * token
     */
    private String token;

    /**
     * 手机号码
     * phone
     */
    private String phone;

    /**
     * 邮箱地址
     * email
     */
    private String email;

    /**
     * 验证码
     * vcode
     */
    private String vcode;

    /**
     * 校验码
     * valid_code
     */
    private String validCode;

    /**
     * 有效时间
     * effective_time
     */
    private Long effectiveTime;

    /**
     * 校验时间
     * valid_time
     */
    private Long validTime;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;
}