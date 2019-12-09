package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-06-21 15:31
 */
@Data
@Accessors(chain = true)
@TableName("scf_cfg_business_work_flow_file")
public class BusinessWorkFlowFile {
    /**
     * 
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
     * 文件配置ID
     * business_file_id
     */
    private Long businessFileId;

    /**
     * 流程扭转ID
     * work_flow_id
     */
    private Long workFlowId;

    /**
     * 是否必传
     * required
     */
    private Integer required;

    /**
     * 排序
     * sort
     */
    private Integer sort;

    /**
     * 是否有效(1:有效;2无效)
     * status
     */
    private Integer status;

    /**
     * 
     * update_time
     */
    private Date updateTime;

    /**
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * create_time
     */
    private Date createTime;

    /**
     * 
     * create_by
     */
    private Long createBy;
}