package com.zhjs.scfcloud.app.controller;

import com.zhjs.scfcloud.app.feign.BusinessFeignService;
import com.zhjs.scfcloud.app.service.BusinessCfgService;
import com.zhjs.scfcloud.app.util.MySubjectUtil;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessAttrDTO;
import com.zhjs.scfcloud.model.dto.business.QueryWorkFlowCfgDTO;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话功能简述>
 * <业务相关配置接口>
 * Version: 1.0.0, 2019-06-11 17:06
 *
 * @author liuchanghai
 * @create 2019-06-11 17:06
 * @since
 */

@Api(tags = "Api接口")
@RestController
@RequestMapping("/business/")
public class BusinessController {

    private Logger logger = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private BusinessFeignService businessFeignService;
    @Autowired
    private BusinessCfgService businessCfgService;

    @ApiOperation("获取用户信息")
    @PostMapping("getBusinessAttr")
    public String getBusinessAttr(@RequestBody QueryBusinessAttrDTO dto){
        return null;
    }

    @ApiOperation("保存用户信息")
    @PostMapping("saveCustomer")
    public Result saveCustomer(@RequestBody CustomerDTO dto) {
        return businessFeignService.saveCustomer(dto);
    }

    @ApiOperation("查询业务属性字段配置信息")
    @PostMapping("findBusinessAttrCfg")
    public String findBusinessAttrCfg(@RequestBody QueryBusinessAttrDTO dto){
        return businessCfgService.getBusinessFormCfg(dto).toJSON();
    }

    @ApiOperation("查询后续流程配置信息")
    @PostMapping("getTrailingWorkFlowCfg")
    public String getTrailingWorkFlowCfg(@RequestBody QueryWorkFlowCfgDTO dto){
        logger.info("查询后续流程配置信息:{}", JsonUtil.toJSON(dto));
        dto.setCompanyId(MySubjectUtil.getCompanyId());
        dto.setCompanyName(MySubjectUtil.getCompanyName());
        return businessCfgService.getTrailingWorkFlowCfg(dto).toJSON();
    }
    @ApiOperation("查询公司所有业务类型")
    @PostMapping("getBusinessTypeList")
    public String getBusinessTypeList(){
        logger.info("subject:{}","查询公司所有业务类型");
        if (MySubjectUtil.getCompanyId() == null)
            return Result.failure("未获取到登录信息").toJSON();
        return businessCfgService.getBusinessTypeList(MySubjectUtil.getCompanyId());
    }
}
