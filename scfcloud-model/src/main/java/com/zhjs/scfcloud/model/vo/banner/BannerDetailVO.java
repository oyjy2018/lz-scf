package com.zhjs.scfcloud.model.vo.banner;

import com.zhjs.scfcloud.util.util.FileUtil;
import lombok.Data;

/**
 * @author:dailongting
 * @date:2019-11-18 15:06
 */
@Data
public class BannerDetailVO {
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
     * banner文件地址
     * banner_file_url
     */
    private String bannerFileUrl;

    /**
     * banner文件预览地址
     * banner_file_url
     */
    private String bannerFileViewUrl;


    /**
     * 启用状态(0:禁用,1:启用)
     * use_status
     */
    private Integer useStatus;

    public String getBannerFileViewUrl() {
        return bannerFileUrl = FileUtil.getViewFileUrl(bannerFileUrl);
    }
}