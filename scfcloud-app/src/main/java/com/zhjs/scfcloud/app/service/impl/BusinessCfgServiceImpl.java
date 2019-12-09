package com.zhjs.scfcloud.app.service.impl;

import com.zhjs.scfcloud.app.feign.BusinessFeignService;
import com.zhjs.scfcloud.app.service.BusinessCfgService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.QueryBusinessAttrDTO;
import com.zhjs.scfcloud.model.dto.business.QueryWorkFlowCfgDTO;
import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import com.zhjs.scfcloud.model.entity.BusinessFlow;
import com.zhjs.scfcloud.model.entity.BusinessType;
import com.zhjs.scfcloud.model.entity.BusinessWorkFlow;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * 业务相关配置逻辑处理类
 * @author: dailongting
 * @date:2019/6/13 10:39
 */
@Service
public class BusinessCfgServiceImpl implements BusinessCfgService {

    @Autowired
    BusinessFeignService businessFeignService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Result getBusinessFormCfg(QueryBusinessAttrDTO dto) {
        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        List<BusinessAttrVal> attrValList = BusinessCfgUtil.getBusinessAttrValList(dto.getCompanyId(),businessType.getId(),null,status1.getStatus());

        //包装表单配置数据传给前端
        List<BusinessAttrCfgVO> businessAttrList = BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),businessType.getId(), status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");

        List<Map<String, Object>> fromAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, dto.getQueryType());
        Map<String, Object> data = new HashMap<>();
        data.put("attrValList", attrValList);
        data.put("formAttrList", fromAttrList);
        return Result.success(data);
    }

    @Override
    public Result getTrailingWorkFlowCfg(QueryWorkFlowCfgDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        BusinessFlow businessFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(dto.getFlowCode(), businessFlowList);

        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),businessType.getId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+businessFlow.getFlowName()+"】获取后续流程失败");

        List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode(dto.getFlowCode(), workFlowCfgList);
        if(trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()) return  Result.failure("流程【"+businessFlow.getFlowName()+"】获取扭转配置失败");

        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),businessType.getId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");

        List<Map<String, Object>> fromAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, CommonEnum.QueryType.AUDIT.getStatus());
        Map<String, Object> data = new HashMap<>();
        data.put("trailingWorkFlowCfg", trailingWorkFlowCfg);
        data.put("fromAttrList", fromAttrList);

        return Result.success(data);
    }


    //获取授信申请的业务类型id
    @Override
    public String getBusinessTypeList(Long companyId) {
        //查询业务类型信息
        List<BusinessType> businessTypeList = BusinessCfgUtil.getBusinessTypeList(companyId,CommonEnum.WhetherEnum.YES.getStatus());
        if (businessTypeList == null || businessTypeList.isEmpty())
            return Result.failure("无业务类型信息").toJSON();
        Vector<Map> vector = new Vector<>();
        vector = businessTypeList.stream().map(businessType -> {
                  Map map = new HashMap();
                  map.put("businessType",businessType.getBusinessName());
                  map.put("businessTypeId",businessType.getId());
                  //查询对应业务的状态
                  List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(companyId,businessType.getId(),CommonEnum.WhetherEnum.YES.getStatus());
                  if (businessFlowList != null && !businessFlowList.isEmpty()) {
                      for (BusinessFlow businessFlow:businessFlowList) {
                           if (businessFlow.getFlowType().intValue() == 0) {
                               map.put("beginFlowCode",businessFlow.getFlowCode());
                               break;
                           }
                      }
                  }
                  return map;
                }).collect(Collectors.toCollection(Vector::new));
        Map retMap = new HashMap();
        retMap.put("businessList",vector);
        return Result.success(retMap).toJSON();
    }
}
