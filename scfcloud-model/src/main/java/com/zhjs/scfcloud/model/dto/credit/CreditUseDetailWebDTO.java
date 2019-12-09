package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  我的用信详情 参数 web
 */
@Data
public class CreditUseDetailWebDTO  {

    //用信类型
    @NotNull(message = "用信类型不能为空")
    private Integer useType;

    //用信id
    @NotNull(message = "用信id不能为空")
    private Long id;

}
