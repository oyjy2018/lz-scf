package com.zhjs.scfcloud.model.vo.banner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhjs.scfcloud.util.util.FileUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author:dailongting
 * @date:2019-11-18 15:06
 */
@Data
public class BannerListVO {
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
     * create_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    public String getBannerFileUrl() {
        if (!StringUtil.isEmpty(bannerFileUrl)) {
            bannerFileUrl = FileUtil.getViewFileUrl(bannerFileUrl);
        }
        return bannerFileUrl;
    }
}