package com.zhjs.scfcloud.service.impl;

import com.zhjs.scfcloud.feign.BusinessFeignService;
import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.business.*;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.util.BusinessCfgUtil;
import com.zhjs.scfcloud.model.util.DictionaryUtil;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.service.BusinessCfgService;
import com.zhjs.scfcloud.util.enums.BusinessFlowEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.RedisUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public Result findAllBusinessFileCfg() {
        return businessFeignService.findAllBusinessFileCfg();
    }

    @Override
    public Result findFileCfgByWorkFlowIds(QueryBusinessFileCfgDTO dto) {
        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), "申请授信");
        if(businessType == null){
            return Result.failure("无申请授信业务配置数据");
        }
        List<BusinessFileCfg> fileCfgList =
                BusinessCfgUtil.getBusinessFileCfgList(dto.getCompanyId(),businessType.getId(),CommonEnum.StatusEnnum.status1.getStatus());
        if(fileCfgList == null) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");
        return Result.success(fileCfgList);
    }

    //获取业务的流程状态集合
    @Override
    public String getFlowCodeList(Long businessTypeId) {
        List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(businessTypeId,CommonEnum.WhetherEnum.YES.getStatus());
        if (ListUtil.isEmpty(businessFlowList))
            return Result.success(businessFlowList).toSerializerJSON();
        List<Map> retList = businessFlowList.stream().map(businessFlow -> {
            Map map = new HashMap();
            map.put("flowCode",businessFlow.getFlowCode());
            map.put("flowName",businessFlow.getFlowName());
            return map;
        }).collect(Collectors.toList());

        return Result.success(retList).toSerializerJSON();
    }

    //获取业务类型id
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

    @Override
    public Result getStandardBusinessTypeList(Long companyId) { //查询业务类型信息
        List<BusinessType> businessTypeList = BusinessCfgUtil.getBusinessTypeList(companyId,CommonEnum.WhetherEnum.YES.getStatus());
        if (businessTypeList == null || businessTypeList.isEmpty())
            return Result.failure("无业务类型信息");
        Vector<Map> vector = new Vector<>();
        vector = businessTypeList.stream().map(businessType -> {
            Map map = new HashMap();
            String businessName = businessType.getBusinessName();
            if ("申请授信".equals(businessName)){
                businessName = "授信审批业务";
            } else if ("申请开商票".equals(businessName)) {
                businessName = "用信审批业务-开商票";
            }
            map.put("businessType",businessName);
            map.put("businessTypeId",businessType.getId());
            return map;
        }).collect(Collectors.toCollection(Vector::new));
        Map retMap = new HashMap();
        retMap.put("businessList",vector);
        return Result.success(retMap);
    }

    @Override
    public String getBusinessFlowForward(QueryBusinessCfgDTO dto) {
        List<BusinessFlow> businessFlowList = BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessFlowList == null || businessFlowList.isEmpty()){
            return Result.failure("无业务流程数据").toJSON();
        }

        List<Map> forwardFlow = businessFlowList.stream().filter(flow -> BusinessFlowEnum.FlowType.hasForwardFlow(flow.getFlowType())).map(businessFlow -> {
            Map map = new HashMap();
            map.put("flowCode",businessFlow.getFlowCode());
            map.put("flowName",businessFlow.getFlowName());
            return map;
        }).collect(Collectors.toList());;
        return Result.success(forwardFlow).toJSON();
    }

    @Override
    public Result getBusinessFlowExtendAll(BusinessTypeBaseDTO dto) {
        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(dto.getCompanyId(),dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus());
        if(businessFlowList == null || businessFlowList.isEmpty()){
            return Result.failure("获取业务流程配置失败");
        }

        //获取流程扭转配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),dto.getBusinessTypeId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()){
            return Result.failure("获取流程扭转配置失败");
        }

        //组装流程集合
        List<Map<String, Object>> flowList = new ArrayList<>();
        businessFlowList.forEach(businessFlow -> {
            Map<String, Object> flow = new HashMap<>();
            flow.put("flowId", businessFlow.getId());
            flow.put("flowName", businessFlow.getFlowName());
            flow.put("flowCode", businessFlow.getFlowCode());
            flow.put("flowType", businessFlow.getFlowType());
            flowList.add(flow);
        });

        //组装各流程配置集合
        List<Map<String, Object>> flowCfgList = new ArrayList<>();
        businessFlowList.forEach(businessFlow -> {
            Map<String, Object> flowCfg = new HashMap<>();
            flowCfg.put("flowId", businessFlow.getId());
            flowCfg.put("flowName", businessFlow.getFlowName());
            flowCfg.put("flowCode", businessFlow.getFlowCode());
            flowCfg.put("flowType", businessFlow.getFlowType());
            //获取当前流程的所有可能扭转流程的配置
            List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode(businessFlow.getFlowCode(), workFlowCfgList);
            List<Map<String, Object>> flowExtendList = new ArrayList<>();
            trailingWorkFlowCfg.forEach(twfc -> {
                Map<String, Object> flowExtend = new HashMap<>();
                BusinessFlow afterFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(twfc.getAfterFlow(),businessFlowList);
                flowExtend.put("workFlowId", twfc.getId());
                flowExtend.put("flowId",afterFlow.getId());
                flowExtend.put("flowName",afterFlow.getFlowName());
                flowExtend.put("flowCode", afterFlow.getFlowCode());
                flowExtendList.add(flowExtend);
            });
            flowCfg.put("flowExtendList", flowExtendList);
            flowCfgList.add(flowCfg);
        });
        Map<String, Object> jMap = new HashMap<>();
        jMap.put("flowList", flowList);
        jMap.put("flowCfgList", flowCfgList);
        return Result.success(jMap);
    }

    @Override
    public Result editWorkFlow(EditWorkFlowDTO dto) {
        return businessFeignService.editWorkFlow(dto);
    }

    @Override
    public Result workFlowExists(WorkFlowExistsDTO dto) {
        BusinessWorkFlow workFlow =
                BusinessCfgUtil.getBusinessWorkFlow(dto.getCompanyId(), dto.getBusinessTypeId(), CommonEnum.StatusEnnum.status1.getStatus(), dto.getBeforeFlow(), dto.getAfterFlow());
        boolean isExists = workFlow == null ? false:true;
        Map<String, Object> jMap = new HashMap<>();
        jMap.put("isExists", isExists);
        return Result.success(jMap);
    }

    @Override
    public Result findFlowExtendSettingInfo(WorkFlowExistsDTO dto) {
        //获取流程扭转配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(dto.getCompanyId(),dto.getBusinessTypeId());
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()){
            return Result.failure("获取流程扭转配置失败");
        }

        BusinessWorkFlowExtendVO workFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtend(dto.getBeforeFlow(),dto.getAfterFlow(), workFlowCfgList);
        if(workFlowCfg == null){
            return Result.failure("当前选择流程扭转配置不存在");
        }

        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;
//        BusinessType businessType = BusinessCfgUtil.getBusinessType(dto.getCompanyId(), dto.getBusinessTypeId());
//        if(businessType == null){
//            return Result.failure("无业务类型配置数据");
//        }

        //查询所有字段配置
        List<BusinessAttrCfgVO> businessAttrCfgList = BusinessCfgUtil.getBusinessAttrCfgVOList(dto.getCompanyId(),dto.getBusinessTypeId(), status1.getStatus());
        if(businessAttrCfgList == null || businessAttrCfgList.isEmpty()) {
            return Result.failure("无业务字段配置数据");
        }

        Map<String,List<BusinessAttrVal>> businessAttrValMap = BusinessCfgUtil.getBusinessAttrValList(dto.getCompanyId(),dto.getBusinessTypeId(), status1.getStatus());
        if(businessAttrValMap == null || businessAttrValMap.isEmpty()) {
            return Result.failure("无业务字段封闭式值配置数据");
        }

        List<BusinessFileCfg> businessfileCfgList = BusinessCfgUtil.getBusinessFileCfgList(dto.getCompanyId(), dto.getBusinessTypeId(), status1.getStatus());
        if(businessfileCfgList == null || businessfileCfgList.isEmpty()) {
            return Result.failure("无业务附件配置数据");
        }

        // 组装配置页面字段值下拉框数据
        List<Map<String, Object>> businessAttrList = new ArrayList<>();
        businessAttrList.add(new HashMap<String, Object>(){{
            put("columnName", "");
            put("columnCh", "-- 请选择 --");
        }});
        businessAttrCfgList.stream().filter(attrCfg -> {
            return BusinessCfgUtil.hasAssignTypeAttCfg(attrCfg, CommonEnum.QueryType.AUDIT.getStatus())
                    || BusinessCfgUtil.hasAssignTypeAttCfg(attrCfg, CommonEnum.QueryType.CREATE.getStatus()) ;
        }).forEach(v -> {
            Map<String, Object> attrCfg = new HashMap<>();
            attrCfg.put("columnName", v.getColumnName());
            attrCfg.put("columnCh", v.getColumnCh());
            attrCfg.put("hasChecked", false);
            businessAttrList.add(attrCfg);
        });

        // 组装配置页面字段下拉框数据
        List<Map<String, Object>> auditAttrList = new ArrayList<>();
        auditAttrList.add(new HashMap<String, Object>(){{
            put("id", 0);
            put("columnCh", "-- 请选择 --");
            put("hasChecked", false);
        }});
        businessAttrCfgList.stream().filter(attrCfg -> {
            // 只封装审核字段
            return BusinessCfgUtil.hasAssignTypeAttCfg(attrCfg, CommonEnum.QueryType.AUDIT.getStatus())
                    && !BusinessCfgUtil.hasAssignTypeAttCfg(attrCfg, CommonEnum.QueryType.CREATE.getStatus()) ;
        }).forEach(v -> {
            Map<String, Object> attrCfg = new HashMap<>();
            attrCfg.put("id", v.getBusinessAttrId());
            attrCfg.put("columnCh", v.getColumnCh());
            attrCfg.put("hasClosedType", v.getHasClosedType());
            attrCfg.put("hasChecked", false);
            auditAttrList.add(attrCfg);
        });

        // 组装配置页面字段封闭式值数据
        Map<String, List<Map<String, Object>>> attrValMap = new HashMap<>();
        Set<String> keySet = businessAttrValMap.keySet();
        for(String key: keySet){
            List<Map<String, Object>> valList = new ArrayList<>();
            businessAttrValMap.get(key).forEach(attrVal -> {
                Map<String, Object> valMap = new HashMap<>();
                valMap.put("id", attrVal.getId());
                valMap.put("valueCh", attrVal.getValueCh());
                valList.add(valMap);
            });
            attrValMap.put(key, valList);
        }

        // 组装配置页面默认值类型下拉框数据
        List<DictionaryDetails> detailsList = DictionaryUtil.getDictionaryDetailsByParentKey("default_value_type");
        List<Map<String, Object>> defaultValueTypeList = new ArrayList<>();
        detailsList.stream()
                .filter(details -> details.getDictKey().equals("column_value") || details.getDictKey().equals("fixed_value"))
                .forEach(details -> {
            Map<String, Object> defaultValueType = new HashMap<>();
            defaultValueType.put("dickKey", details.getDictKey());
            defaultValueType.put("dickValue", details.getDictValue());
            defaultValueTypeList.add(defaultValueType);
        });

        // 组装配置页面附件下拉框数据
        List<Map<String, Object>> fileCfgList = new ArrayList<>();
        fileCfgList.add(new HashMap<String, Object>(){{
            put("id", 0);
            put("fileName", "-- 请选择 --");
            put("hasChecked", false);
        }});
        businessfileCfgList.forEach(businessFileCfg -> {
            Map<String, Object> fileCfg = new HashMap<>();
            fileCfg.put("id", businessFileCfg.getId());
            fileCfg.put("fileName", businessFileCfg.getFileName());
            fileCfg.put("hasChecked", false);
            fileCfgList.add(fileCfg);
        });

        // 组装流程扭转字段配置数据
        List<Map<String, Object>> editAttrCfgData = new ArrayList<>();
        if(workFlowCfg.getBusinessAttrCfgVOList() != null){
            workFlowCfg.getBusinessAttrCfgVOList().forEach(attrCfg -> {
                Map<String, Object> editAttrCfg = new HashMap<>();
                editAttrCfg.put("workFlowId", workFlowCfg.getId());
                editAttrCfg.put("businessAttrId", attrCfg.getBusinessAttrId());
                editAttrCfg.put("hasClosedType", attrCfg.getHasClosedType());
                editAttrCfg.put("required", CommonEnum.WhetherEnum.YES.getStatus().intValue() == attrCfg.getRequired().intValue());
                editAttrCfg.put("defaultValueType", attrCfg.getDefaultValueType() == null ? "": attrCfg.getDefaultValueType());
                editAttrCfg.put("defaultValue", attrCfg.getDefaultValue() == null ? "":attrCfg.getDefaultValue());
                editAttrCfgData.add(editAttrCfg);
            });
        }

        // 组装流程扭转字段配置数据
        List<Map<String, Object>> editFileCfgData = new ArrayList<>();
        if(workFlowCfg.getWorkFlowFileVOList() != null){
            workFlowCfg.getWorkFlowFileVOList().forEach(fileCfg -> {
                Map<String, Object> editFileCfg = new HashMap<>();
                editFileCfg.put("workFlowId", workFlowCfg.getId());
                editFileCfg.put("fileId", fileCfg.getBusinessFileId());
                editFileCfg.put("required", CommonEnum.WhetherEnum.YES.getStatus().intValue() == fileCfg.getRequired().intValue());
                editFileCfgData.add(editFileCfg);
            });
        }

        Map<String, Object> jMap = new HashMap<>();
        jMap.put("businessAttrList", businessAttrList);
        jMap.put("auditAttrList", auditAttrList);
        jMap.put("attrValMap", attrValMap);
        jMap.put("defaultValueTypeList", defaultValueTypeList);
        jMap.put("fileCfgList", fileCfgList);
        jMap.put("editAttrCfgData", editAttrCfgData);
        jMap.put("editFileCfgData", editFileCfgData);
        jMap.put("roleIds", StringUtil.stringConvertLong(workFlowCfg.getRoleIds()));
        jMap.put("userIds", StringUtil.stringConvertLong(workFlowCfg.getUserIds()));
        return Result.success(jMap);
    }

    @Override
    public Result savePower(SavePowerDTO dto) {
        return businessFeignService.savePower(dto);
    }
}
