package com.zhjs.scfcloud.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.BusinessBaseDTO;
import com.zhjs.scfcloud.model.dto.business.EditWorkFlowDTO;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessFileCfgDTO;
import com.zhjs.scfcloud.model.dto.business.SavePowerDTO;
import com.zhjs.scfcloud.model.entity.BusinessFileCfg;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.system.service.BusinessAttrService;
import com.zhjs.scfcloud.system.service.BusinessFileCfgService;
import com.zhjs.scfcloud.system.service.BusinessService;
import com.zhjs.scfcloud.system.service.InitFlowRedisService;
import com.zhjs.scfcloud.util.util.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: dailongting
 * @date:2019/6/12 16:57
 */
@RestController
@RequestMapping("/business")
public class BusinessController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusinessAttrService businessAttrService;
    @Autowired
    private BusinessFileCfgService businessFileCfgService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    InitFlowRedisService initFlowRedisService;

    /**
     * 查询业务属性字段配置信息
     * @return
     */
    @PostMapping("/findBusinessAttrCfg")
    public Result<List<BusinessAttrCfgVO>> findBusinessAttrCfg(){
        logger.info("查询业务属性字段配置信息");
        try {
            List<BusinessAttrCfgVO> businessAttrCfgVOList = businessAttrService.findBusinessAttrCfg();
            if(businessAttrCfgVOList == null || businessAttrCfgVOList.isEmpty()) return Result.failure("业务属性字段配置信息丢失！");
            return Result.success(businessAttrCfgVOList);
        }catch (Exception e){
            logger.error("查询业务属性字段配置信息异常：{}",e.getMessage());
            return Result.failure("查询业务属性字段配置信息异常");
        }
    }

    @ApiOperation("查询流程扭转附件配置")
    @PostMapping("/findAllBusinessFileCfg")
    public Result findAllBusinessFileCfg(){

        return Result.success(
                businessFileCfgService.list(new QueryWrapper<BusinessFileCfg>()

                ));
    }

    @ApiOperation("初始公司流程权限-标准化配置")
    @PostMapping("/initCompanyFlowPermit")
    public Result initCompanyFlowPermit(@RequestParam Long companyId){
        logger.info("subject:{},companyId:{}","初始公司流程权限-标准化配置",companyId);
        return businessService.initCompanyFlowPermit(companyId);
    }

    @ApiOperation("编辑业务流程扭转配置")
    @PostMapping("/editWorkFlow")
    public Result editWorkFlow(@RequestBody EditWorkFlowDTO dto){
        logger.info("subject:{},dto:{}","编辑业务流程扭转配置", JsonUtil.toJSON(dto));
        return businessService.editWorkFlow(dto);
    }

    @ApiOperation("编辑业务流程扭转权限、字段与附件配置")
    @PostMapping("/savePower")
    public Result savePower(@RequestBody SavePowerDTO dto){
        logger.info("subject:{},dto:{}","编辑业务流程扭转权限、字段与附件配置", JsonUtil.toJSON(dto));
        Result result = businessService.savePower(dto);
//        if(result.getCode() == Result.RESULT_CODE_SUCCESS){
//            // 更新缓存
//            initFlowRedisService.initFlowRedis();
//        }
        return result;
    }
}

