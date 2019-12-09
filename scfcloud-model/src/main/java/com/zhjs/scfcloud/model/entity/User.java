package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author:weijie.chen
 * @date:2019-07-21 16:51
 */
@Data
@Accessors(chain = true)
@TableName("scf_user")
public class User implements Serializable {
    /**
     * 主键ID
     * id
     */
    private Long id;

    /**
     * 手机号
     * phone
     */
    private String phone;

    /**
     * 邮箱
     * email
     */
    private String email;

    /**
     * 密码
     * password
     */
    private String password;

    /**
     * 身份证号
     * id_card
     */
    private String idCard;

    /**
     * 用户名
     * user_name
     */
    private String userName;

    /**
     * 性别(0:未知 1:男 2:女)
     * gender
     */
    private Integer gender;

    /**
     * 邮箱是否验证(0:否 1：是)
     * is_email_valid
     */
    private Integer isEmailValid;

    /**
     * 最后一次选择的公司ID
     * last_company_id
     */
    private Long lastCompanyId;

    /**
     * 机构类型（1：个人；2：机构）
     * permission_type
     */
    private Integer permissionType;

    /**
     * 权限机构集合（逗号分割）
     * permission_org_ids
     */
    private String permissionOrgIds;

    /**
     * 修改人ID
     * update_by_id
     */
    private Long updateById;

    /**
     * 修改时间
     * update_time
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     * create_by_id
     */
    private Long createById;

    /**
     * 创建时间
     * create_time
     */
    private LocalDateTime createTime;

    /**
     * 删除标记0未删除1已删除
     * is_del
     */
    @TableLogic
    private Integer isDel;
}