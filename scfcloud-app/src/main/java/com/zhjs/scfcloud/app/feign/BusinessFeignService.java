package com.zhjs.scfcloud.app.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.IdCardDTO;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-11 17:08
 *
 * @author liuchanghai
 * @create 2019-06-11 17:08
 * @since
 */

@FeignClient(value = "scfcloud-system", contextId = "business")
public interface BusinessFeignService {

    @ApiOperation("获取用户信息")
    @PostMapping("/customer/findByIdCard")
    Result findCustomerByIdCard(@RequestBody IdCardDTO dto);

    @ApiOperation("保存用户信息")
    @PostMapping("/customer/save")
    Result saveCustomer(@RequestBody CustomerDTO dto);

    @ApiOperation("查询业务属性字段配置信息")
    @PostMapping("/business/findBusinessAttrCfg")
    Result<List<BusinessAttrCfgVO>> findBusinessAttrCfg();
}
