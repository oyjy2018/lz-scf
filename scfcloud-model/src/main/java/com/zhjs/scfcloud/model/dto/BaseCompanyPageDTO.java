package com.zhjs.scfcloud.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 当前公司的列表 分页参数dto
 */

@Data
public class BaseCompanyPageDTO extends Page {

    //用户id
    private Long companyId;
}
