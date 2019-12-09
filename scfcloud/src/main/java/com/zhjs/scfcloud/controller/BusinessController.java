package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.BusinessFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.CustomerDTO;
import com.zhjs.scfcloud.model.dto.SingleParamDTO;
import com.zhjs.scfcloud.model.dto.business.*;
import com.zhjs.scfcloud.model.entity.BusinessType;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.BusinessCfgService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.DateUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查询业务属性字段配置信息(授信业务用)")
    @PostMapping("findBusinessAttrCfg")
    @RequiresPermissions("risk:credit:apply:findConfig")
    public String findBusinessAttrCfg(@RequestBody QueryBusinessAttrDTO dto){
        logger.info("subject:{},dto:{}","查询业务属性字段配置信息", JsonUtil.toJSON(dto));
        UserInfoVO user = MySubjectUtil.getUser();
        dto.setCompanyId(user.getCompanyId());
        dto.setCompanyName(user.getCompanyName());
        return businessCfgService.getBusinessFormCfg(dto).toJSON();
    }

//    @ApiOperation("查询指定流程扭转附件配置")
//    @PostMapping("/findFileCfgByWorkFlowIds")
//    public String findFileCfgByWorkFlowIds(@RequestBody QueryBusinessFileCfgDTO dto){
//        logger.info("subject:{},dto:{}","查询指定流程扭转附件配置", JsonUtil.toJSON(dto));
//        UserInfoVO user = MySubjectUtil.getUser();
//        dto.setCompanyId(user.getCompanyId());
//        dto.setCompanyName(user.getCompanyName());
//        return businessCfgService.findFileCfgByWorkFlowIds(dto).toJSON();
//    }

    @ApiOperation("获取业务的流程状态集合")
    @PostMapping("/getFlowCodeList") //只传businessTypeId
    public String getFlowCodeList(@RequestBody SingleParamDTO dto){
        logger.info("subject:{},dto:{}","获取业务的流程状态集合", JsonUtil.toJSON(dto));
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();

        BusinessType businessType = BusinessCfgUtil.getBusinessType(user.getCompanyId(), dto.getParam());
        if(businessType == null) return Result.failure("无"+dto.getParam()+"业务配置数据").toJSON();

        return businessCfgService.getFlowCodeList(businessType.getId());
    }

    @ApiOperation("查询公司所有业务类型")
    @PostMapping("getBusinessTypeList")
    public String getBusinessTypeList(){
        logger.info("subject:{}","查询公司所有业务类型");
        if (MySubjectUtil.getUser() == null)
            return Result.failure("未获取到登录信息").toJSON();
        return businessCfgService.getBusinessTypeList(MySubjectUtil.getUser().getCompanyId());
    }


    @ApiOperation("初始公司流程权限-标准化配置")
    @PostMapping("initCompanyFlowPermit")
    public Result initCompanyFlowPermit(@RequestParam Long companyId,@RequestParam String key) {
        logger.info("subject:{},companyId:{},key:{}","初始公司流程权限-标准化配置",companyId,key);
        if (!DateUtil.getTodayDateStr("yyyyMMdd").concat(companyId%4+"").equals(key)) return Result.failure("秘钥不正确");
        return businessFeignService.initCompanyFlowPermit(companyId);
    }

    @ApiOperation("查询正向流程列表")
    @PostMapping("getBusinessFlowForward")
    public String getBusinessFlowForward(@RequestBody QueryBusinessCfgDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","查询正向流程列表",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        return businessCfgService.getBusinessFlowForward(dto);
    }

    @ApiOperation("查询业务流程扭转配置")
    @PostMapping("getBusinessFlowExtendAll")
    public String getBusinessFlowExtendAll(@RequestBody BusinessTypeBaseDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","查询业务流程扭转配置",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        return businessCfgService.getBusinessFlowExtendAll(dto).toJSON();
    }

    @ApiOperation("编辑业务流程扭转配置")
    @PostMapping("/editWorkFlow")
    public String editWorkFlow(@RequestBody EditWorkFlowDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","编辑业务流程扭转配置",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        dto.setUserId(user.getId());
        dto.setUserName(user.getUserName());
        return businessCfgService.editWorkFlow(dto).toJSON();
    }

    @ApiOperation("校验流程扭转配置是否存在")
    @PostMapping("/workFlowExists")
    public String workFlowExists(@RequestBody WorkFlowExistsDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","校验流程扭转配置是否存在",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        return businessCfgService.workFlowExists(dto).toJSON();
    }

    @ApiOperation("查询流程扭转配置信息")
    @PostMapping("/findFlowExtendSettingInfo")
    public String findFlowExtendSettingInfo(@RequestBody WorkFlowExistsDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},dto:{}","查询流程扭转配置信息",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        return businessCfgService.findFlowExtendSettingInfo(dto).toJSON();
    }

    @ApiOperation("编辑业务流程扭转权限、字段与附件配置")
    @PostMapping("/savePower")
    public String savePower(@RequestBody SavePowerDTO dto, BindingResult bindingResult) {
        logger.info("subject:{},dto:{}","编辑业务流程扭转权限、字段与附件配置",JsonUtil.toJSON(dto));
        if (bindingResult.hasErrors())
            return Result.failure(bindingResult.getFieldError().getDefaultMessage()).toJSON();
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("没有登录信息").toJSON();
        dto.setCompanyId(user.getCompanyId());
        dto.setUserId(user.getId());
        return businessCfgService.savePower(dto).toJSON();
    }

}
