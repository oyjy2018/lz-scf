package com.zhjs.scfcloud.model.dto.banner;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BannerSortDTO extends Page {

    @NotNull(message = "原位置id不能为空")
    private Long id;

    @NotNull(message = "目标位置id不能为空")
    private Long toId;

    @NotBlank(message = "移动方向不能为空")
    private String direction;

    private Long userId;
}
