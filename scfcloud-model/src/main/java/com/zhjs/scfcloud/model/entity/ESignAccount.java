package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:weijie.chen
 * @date:2019-07-17 18:05
 */
@Data
@Accessors(chain = true)
@TableName("scf_e_sign_account")
public class ESignAccount {
    /**
     * 
     * id
     */
    private Integer id;

    /**
     * 金融云账户类型（0:公司,1:个人）
     * scf_account_type
     */
    private Integer scfAccountType;

    /**
     * 金融云账户id
     * scf_account_id
     */
    private Long scfAccountId;

    /**
     * 对应e签宝账户id
     * e_sign_account_id
     */
    private String eSignAccountId;

    /**
     * 是否软删(0:未删除,1:已删除)
     * is_del
     */
    private Byte isDel;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;

    /**
     * 
     * create_by
     */
    private Long createBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 电子印章图片base64数据
     * seal_data
     */
    private String sealData;
}