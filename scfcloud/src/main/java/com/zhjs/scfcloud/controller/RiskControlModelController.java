package com.zhjs.scfcloud.controller;

import com.zhjs.scfcloud.feign.RiskControlModelFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.BaseDeleteDTO;
import com.zhjs.scfcloud.model.dto.BaseIdDTO;
import com.zhjs.scfcloud.model.dto.BaseSingleUpdateDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelInsertDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelListDTO;
import com.zhjs.scfcloud.model.dto.risk.RiskControlModelUpdateDTO;
import com.zhjs.scfcloud.model.vo.UserInfoVO;
import com.zhjs.scfcloud.service.BusinessCfgService;
import com.zhjs.scfcloud.util.MySubjectUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 风控模型相关
 */
@RestController
@RequestMapping("/risk/model/")
public class RiskControlModelController {
    private Logger logger = LoggerFactory.getLogger(RiskControlModelController.class);

    @Autowired
    private RiskControlModelFeignService riskControlModelFeignService;
    @Autowired
    private BusinessCfgService businessCfgService;

    //获取风控模型列表
    @PostMapping("list")
    @RequiresPermissions("common:riskModel:list")
    public Result list(@RequestBody RiskControlModelListDTO dto){
        logger.info("subject:{},dto:{}","获取风控模型列表", JsonUtil.toSerializerJSON(dto));

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");

        dto.setUserId(user.getId());
        dto.setPermissionType(user.getPermissionType());
        dto.setPermissionOrgIds(user.getPermissionOrgIds());

        return riskControlModelFeignService.list(dto);
    }

    //启用/禁用风控模型（修改风控模型启用状态）
    @PostMapping("updateHasEnabled")
    @RequiresPermissions("common:riskModel:enabled")
    public Result updateHasEnabled(@RequestBody @Valid BaseSingleUpdateDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","启用/禁用风控模型", JsonUtil.toSerializerJSON(dto));

        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUpdateBy(user.getId());

        return riskControlModelFeignService.updateHasEnabled(dto);
    }

    //删除风控模型
    @PostMapping("delete")
    @RequiresPermissions("common:riskModel:delete")
    public Result delete(@RequestBody @Valid BaseDeleteDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","删除风控模型", JsonUtil.toSerializerJSON(dto));

        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());

        return riskControlModelFeignService.delete(dto);
    }

    //添加风控模型
    @PostMapping("insert")
    @RequiresPermissions("common:riskModel:insert")
    public Result insert(@RequestBody @Valid RiskControlModelInsertDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","添加风控模型", JsonUtil.toSerializerJSON(dto));

        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompanyId());

        return riskControlModelFeignService.insert(dto);
    }

    //修改风控模型
    @PostMapping("update")
    @RequiresPermissions("common:riskModel:update")
    public Result update(@RequestBody @Valid RiskControlModelUpdateDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","修改风控模型", JsonUtil.toSerializerJSON(dto));

        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUserId(user.getId());
        dto.setCompanyId(user.getCompanyId());

        return riskControlModelFeignService.update(dto);
    }

    //更新模型公式
    @PostMapping("updateModelFormula")
    @RequiresPermissions("common:riskModel:update")
    public Result updateModelFormula(@RequestBody @Valid BaseSingleUpdateDTO dto, BindingResult bindingResult){
        logger.info("subject:{},dto:{}","更新模型公式", JsonUtil.toSerializerJSON(dto));
        if (bindingResult.hasFieldErrors()) return Result.failure(bindingResult.getFieldError().getDefaultMessage());

        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("未获取到登录信息");
        dto.setUpdateBy(user.getId());

        return riskControlModelFeignService.updateModelFormula(dto);
    }

    //获取模型详情
    @PostMapping("detail")
    @RequiresPermissions("common:riskModel:detail")
    public Result detail(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","获取模型详情", JsonUtil.toSerializerJSON(dto));

        if (StringUtil.isEmpty(dto.getId())) return Result.failure("id不能为空");

        return riskControlModelFeignService.detail(dto.getId());
    }

    //获取模型公式
    @PostMapping("getModelFormula")
    public Result getModelFormula(@RequestBody BaseIdDTO dto){
        logger.info("subject:{},dto:{}","获取模型公式", JsonUtil.toSerializerJSON(dto));

        if (StringUtil.isEmpty(dto.getId())) return Result.failure("id不能为空");

        return riskControlModelFeignService.getModelFormula(dto.getId());
    }

    @PostMapping("getBusinessTypeList")
    public Result getBusinessTypeList(){
        logger.info("subject:{}","获取模型公式");
        UserInfoVO user = MySubjectUtil.getUser();
        if (user == null) return Result.failure("无登录信息");
        return businessCfgService.getStandardBusinessTypeList(user.getCompanyId());
    }


}
