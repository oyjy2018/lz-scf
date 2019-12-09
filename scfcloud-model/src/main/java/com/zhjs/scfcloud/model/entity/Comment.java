package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-06-22 16:45
 */
@Data
@Accessors(chain = true)
@TableName("scf_comment")
public class Comment {
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
     * 业务ID
     * business_id
     */
    private Long businessId;

    /**
     * 业务类型ID
     * business_type_id
     */
    private Long businessTypeId;

    /**
     * 流程CODE
     * flow_code
     */
    private String flowCode;

    /**
     * 在状态
     * status_desc
     */
    private String statusDesc;

    /**
     * 评论内容
     * content
     */
    private String content;

    /**
     * 创建人
     * create_by
     */
    private Long createBy;

    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
}