package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import java.util.List;

/**
 *  我的授信申请列表dto
 */
@Data
public class CreditApplyMyListQueryDTO extends Page {

    //公司ID
    private Long companyId;

    private Long userId;

    private String roleIds;

    //流程code
    private String flowCode;

    //客户姓名
    private String customerName;

    private Long businessId;

    private String itemName;

    //是否查询待办 默认否
    private String isWait = "false";

    //客户端
    private String client;

}
