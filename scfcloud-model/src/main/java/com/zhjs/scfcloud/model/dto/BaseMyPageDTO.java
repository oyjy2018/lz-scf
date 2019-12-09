package com.zhjs.scfcloud.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 我的列表 分页参数dto
 */

@Data
public class BaseMyPageDTO extends Page {

    //用户id
    private Long userId;
}
