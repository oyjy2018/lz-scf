package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  我的用信列表参数 web
 */
@Data
public class CreditUseUpdateRefundStatusDTO {

    //用信id
    @NotNull(message = "用信id不能为空")
    private Long id;

    //还款状态
    @NotNull(message = "还款状态不能为空")
    private Integer refundStatus;

    //操作人（取登录用户）
    private Long userId;
}
