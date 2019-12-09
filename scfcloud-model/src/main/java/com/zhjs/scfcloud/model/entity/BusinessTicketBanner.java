package com.zhjs.scfcloud.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:dailongting
 * @date:2019-11-18 15:06
 */
@Data
@Accessors(chain = true)
@TableName("scf_business_ticket_banner")
public class BusinessTicketBanner {
    /**
     * 
     * id
     */
    private Long id;

    /**
     * banner名称
     * banner_name
     */
    private String bannerName;

    /**
     * 跳转链接
     * jump_url
     */
    private String jumpUrl;

    /**
     * 排序
     * sort
     */
    private Integer sort;

    /**
     * banner文件地址
     * banner_file_url
     */
    private String bannerFileUrl;

    /**
     * 启用状态(0:禁用,1:启用)
     * use_status
     */
    private Integer useStatus;

    /**
     * 删除状态(0:未删,1:已删)
     * delete_status
     */
    private Integer deleteStatus;

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
     * 
     * update_by
     */
    private Long updateBy;

    /**
     * 
     * update_time
     */
    private Date updateTime;
}