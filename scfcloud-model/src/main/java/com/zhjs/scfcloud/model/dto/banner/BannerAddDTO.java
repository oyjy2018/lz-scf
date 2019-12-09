package com.zhjs.scfcloud.model.dto.banner;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BannerAddDTO extends Page {

    @NotBlank(message = "banner名称不能为空")
    @Size(max = 100,message = "banner名称不能超过100位")
    private String bannerName;

    @NotBlank(message = "banner图片地址不能为空")
    private String bannerFileUrl;

    @NotBlank(message = "启用状态不能为空")
    private String useStatus;

    @Size(max = 500,message = "banner名称不能超过500位")
    private String jumpUrl;

    private Long userId;
}
