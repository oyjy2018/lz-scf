package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-21 15:33
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_file_cfg")
public class BusinessFileCfg {
    /**
     * id
     * id
     */
    private Long id;

    /**
     * 公司ID
     * company_id
     */
    private Long companyId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 附件类型编码
     * file_code
     */
    private String fileCode;

    /**
     * 附件类型中文名
     * file_name
     */
    private String fileName;

    /**
     * 单次上传大小
     * once_upload_size
     */
    private Integer onceUploadSize;

    /**
     * 上传数量限制
     * upload_limit
     */
    private Integer uploadLimit;

    /**
     * 有权限查看的用户组
     * role_ids
     */
    private String roleIds;

    /**
     * 有权限查的用户
     * user_ids
     */
    private String userIds;

    /**
     * 排序
     * sort
     */
    private Integer sort;

    /**
     * 状态
     * status
     */
    private Integer status;

    /**
     * 更新人
     * update_by
     */
    private Long updateBy;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;
}