package com.zhjs.scfcloud.feign;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.IdCardDTO;
import com.zhjs.scfcloud.model.dto.business.BusinessBaseDTO;
import com.zhjs.scfcloud.model.dto.business.EditWorkFlowDTO;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessFileCfgDTO;
import com.zhjs.scfcloud.model.dto.business.SavePowerDTO;
import com.zhjs.scfcloud.model.entity.BusinessFileCfg;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @ApiOperation("查询流程扭转附件配置")
    @PostMapping("/business/findAllBusinessFileCfg")
    Result findAllBusinessFileCfg();


    @ApiOperation("初始公司流程权限-标准化配置")
    @PostMapping("/business/initCompanyFlowPermit")
    Result initCompanyFlowPermit(@RequestParam Long companyId);

    @ApiOperation("编辑业务流程扭转配置")
    @PostMapping("/business/editWorkFlow")
    Result editWorkFlow(@RequestBody EditWorkFlowDTO dto);

    @ApiOperation("编辑业务流程扭转权限、字段与附件配置")
    @PostMapping("/business/savePower")
    Result savePower(@RequestBody SavePowerDTO dto);
}
