package com.zhjs.scfcloud.model.dto.credit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  我的用信列表参数 app
 */
@Data
public class CreditUseMyListAppDTO extends Page {

    private Long companyId;

    private String idCard;

}
