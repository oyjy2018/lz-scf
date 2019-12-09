package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.credit.CreditUseMyListAppDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-27 12:02
 *
 * @author liuchanghai
 * @create 2019-06-27 12:02
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "creditUse")
public interface CreditUseFeignService {

    @ApiOperation("查看开票申请详情")
    @PostMapping("/creditUse/findAppCreditUseApplyDetails")
    Result findCreditUseApplyDetails(@RequestBody BaseIdDTO dto);

    //我的用信列表
    @PostMapping("creditUse/myAppList")
    String myList(@RequestBody CreditUseMyListAppDTO creditUseMyListAppDTO);
}
