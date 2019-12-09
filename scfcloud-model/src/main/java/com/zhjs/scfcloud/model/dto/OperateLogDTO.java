package com.zhjs.scfcloud.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-22 11:38
 *
 * @author liuchanghai
 * @create 2019-06-22 11:38
 * @since
 */

@Data
public class OperateLogDTO extends Page {

    /**
     * 公司ID
     */
    @NotNull(message = "公司id不能为空")
    private Long companyId;

    /**
     * 业务类型ID
     */
    @NotNull(message = "业务类型id不能为空")
    private Long businessTypeId;

    /**
     * 业务ID
     */
    @NotNull(message = "业务id不能为空")
    private Long businessId;

    /**
     * 操作对象(授信审批;借款审批;附件)
     */
    private String operatObject;
}
