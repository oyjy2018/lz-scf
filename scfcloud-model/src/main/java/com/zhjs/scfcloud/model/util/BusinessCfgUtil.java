package com.zhjs.scfcloud.model.util;

import com.zhjs.scfcloud.model.common.Result;
import com.zhjs.scfcloud.model.dto.credit.AuditFormDTO;
import com.zhjs.scfcloud.model.entity.*;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowExtendVO;
import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;
import com.zhjs.scfcloud.util.constant.RedisConstant;
import com.zhjs.scfcloud.util.enums.BusinessFlowEnum;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.ListUtil;
import com.zhjs.scfcloud.util.util.RedisUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.System;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务配置工具类
 * @author: dailongting
 * @date:2019/6/13 10:15
 */
@Component
public class BusinessCfgUtil {

    private static Logger logger = LoggerFactory.getLogger(BusinessCfgUtil.class);

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        BusinessCfgUtil.redisUtil = redisUtil;
    }

    public static RedisUtil getRedisUtil(){
        return BusinessCfgUtil.redisUtil;
    }

    /**
     * 给审批流转排序 （下一个主流程 要排在首位，“已拒绝”和“当前环节”、“前一环节”的状态不能排在首位）
     * @param trailingWorkFlowCfg
     * @param businessTypeId
     * @param flowCode
     * @return
     */
    public static List<BusinessWorkFlowExtendVO> sortWorkFlowForAudit(List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg, Long businessTypeId, String flowCode) {
        if (ListUtil.isEmpty(trailingWorkFlowCfg)) return trailingWorkFlowCfg;
        //获取业务所有流程
        List<BusinessFlow> businessFlowList = getBusinessFlowList(businessTypeId,CommonEnum.WhetherEnum.YES.getStatus());
        if (ListUtil.isEmpty(businessFlowList)) return trailingWorkFlowCfg;

        //查询业务主流程
        List<BusinessFlow> mainFlowList = businessFlowList.stream().filter(flow -> BusinessFlowEnum.FlowType.hasForwardFlow(flow.getFlowType())).collect(Collectors.toList());
        if (ListUtil.isEmpty(mainFlowList)) return trailingWorkFlowCfg;

        //当前流程
        Optional<BusinessFlow> optional = businessFlowList.stream().filter(flow -> flow.getFlowCode().equals(flowCode)).findAny();
        if (!optional.isPresent()) return trailingWorkFlowCfg;
        BusinessFlow nowFlow = optional.get();

        //下一流程
        BusinessFlow nextMainFlow = null;
        for (int i = 0; i < mainFlowList.size(); i++) {
            BusinessFlow flow = mainFlowList.get(i);
            //排序大于当前流程&小于临时的下一流程
            if (flow.getSort() > nowFlow.getSort() && (nextMainFlow == null || flow.getSort() < nextMainFlow.getSort()) ) {
                nextMainFlow = flow;
            }
        }
        if (nextMainFlow == null) return trailingWorkFlowCfg;

        //筛选主流程配置
        BusinessWorkFlowExtendVO nextWorkFlow = null;
        for(BusinessWorkFlowExtendVO vo:trailingWorkFlowCfg) {
            if (nextMainFlow.getFlowCode().equals(vo.getAfterFlow())) {
                nextWorkFlow = vo;
            }
        }
        if (nextWorkFlow == null) return  trailingWorkFlowCfg;

        //过滤掉主流程
        trailingWorkFlowCfg.remove(nextWorkFlow);

        List<BusinessWorkFlowExtendVO> result = new ArrayList<>();
        result.add(nextWorkFlow);
        result.addAll(1,trailingWorkFlowCfg);
        return  result;
    }

    /**
     * 创建业务附件配置的Map对象（用于缓存存储）
     * @param businessFileCfgList
     * @return
     */
    public static Map<String, Object> createBusinessFileCfgMap(List<BusinessFileCfg> businessFileCfgList){
        if(businessFileCfgList == null || businessFileCfgList.isEmpty()) return null;

        Map<String, Object> businessFileMap = new HashMap<>();
        businessFileCfgList.forEach(businessFileCfg -> {
            Object obj = businessFileMap.get(businessFileCfg.getCompanyId().toString());
            if(obj == null){
                obj = new HashMap<String, Object>();
            }
            Map<String, Object> companyFileMap = ( Map<String, Object>)obj;

            obj = companyFileMap.get(businessFileCfg.getBusinessTypeId().toString());
            if(obj == null){
                obj = new ArrayList<BusinessFileCfg>();
            }
            List<BusinessFileCfg> fileCfgList = (List<BusinessFileCfg>) obj;
            fileCfgList.add(businessFileCfg);
            companyFileMap.put(businessFileCfg.getBusinessTypeId().toString(),fileCfgList);
            businessFileMap.put(businessFileCfg.getCompanyId().toString(),companyFileMap);
        });

        return businessFileMap;
    }

    /**
     * 获取业务文件配置
     * @param companyId
     * @param typeId
     * @param status
     * @return
     */
    public static List<BusinessFileCfg> getBusinessFileCfgList(Long companyId,Long typeId,Integer status){
        Map<String, List<BusinessFileCfg>> fileCfgMap = (Map<String, List<BusinessFileCfg>>)redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_FILE_CFG, companyId.toString());;

        //为NULL返回全部业务类型配置
        if(typeId == null) {
            Set<String> keys = fileCfgMap.keySet();
            List<BusinessFileCfg> fileCfgList = new ArrayList<>();
            keys.forEach(key -> {
                fileCfgList.addAll(fileCfgMap.get(key).stream().filter(fileCfg -> fileCfg.getStatus().intValue() == status.intValue()).collect(Collectors.toList()));
            });
            return fileCfgList;
        }
        if(!fileCfgMap.containsKey(typeId.toString())) return new ArrayList<>();

        List<BusinessFileCfg> fileCfgList = fileCfgMap.get(typeId.toString());
        return fileCfgList.stream().filter(fileCfg -> fileCfg.getStatus().intValue() == status.intValue()).collect(Collectors.toList());
    }

    /**
     * 创建业务属性配置的Map对象（用于缓存存储）
     * @param businessAttrList
     * @return
     */
    public static Map<String, Object> createBusinessAttrCfgMap(List<BusinessAttrCfgVO> businessAttrList){
        if(businessAttrList == null || businessAttrList.isEmpty()) return null;

        Map<String, Object> businessAttrMap = new HashMap<>();
        businessAttrList.forEach(businessAttr -> {
            Object obj = businessAttrMap.get(businessAttr.getCompanyId().toString());
            if(obj == null){
                obj = new HashMap<String, Object>();
            }
            Map<String, Object> companyAttrMap = ( Map<String, Object>)obj;

            obj = companyAttrMap.get(businessAttr.getBusinessTypeId().toString());
            if(obj == null){
                obj = new ArrayList<BusinessAttrCfgVO>();
            }
            List<BusinessAttrCfgVO> attrList = (List<BusinessAttrCfgVO>)obj;
            attrList.add(businessAttr);
            companyAttrMap.put(businessAttr.getBusinessTypeId().toString(),attrList);
            businessAttrMap.put(businessAttr.getCompanyId().toString(), companyAttrMap);
        });

        return businessAttrMap;
    }

    /**
     * 将属性配置根据归类包装成前端表单数据结构
     * @param businessAttrList
     * @return
     */
    public static List<Map<String, Object>> packageFormAttrList(List<BusinessAttrCfgVO> businessAttrList, String queryType){
        if(businessAttrList == null || businessAttrList.isEmpty()) return null;

        List<Map<String, Object>> fromAttrList = new ArrayList<>();
        businessAttrList.forEach(businessAttr -> {
            if(!BusinessCfgUtil.hasAssignTypeAttCfg(businessAttr, queryType)){
                return;
            }

            Map<String, Object> classifyMap =
                    fromAttrList.stream()
                            .filter(classify -> businessAttr.getClassifyName().equals(classify.get("classifyName").toString()))
                            .findAny().orElse(null);
            if(classifyMap == null){
                classifyMap = new HashMap<>();
                classifyMap.put("classifyName", businessAttr.getClassifyName());
                classifyMap.put("attrList", new ArrayList<>());
                fromAttrList.add(classifyMap);
            }

            List<BusinessAttrCfgVO> attrList = (List<BusinessAttrCfgVO>)classifyMap.get("attrList");
            attrList.add(businessAttr);
        });
        return fromAttrList;
    }

    /**
     * 是否是指定类型字段属性配置
     * @param businessAttr
     * @param queryType
     * @return
     */
    public static boolean hasAssignTypeAttCfg(BusinessAttrCfgVO businessAttr, String queryType){
        if(CommonEnum.QueryType.CREATE.getStatus().equals(queryType)){
            if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == businessAttr.getHasCreateNeed().intValue())
                return false;
        }else if(CommonEnum.QueryType.AUDIT.getStatus().equals(queryType)){
            if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == businessAttr.getHasAuditVisible().intValue())
                return false;
        }else if(CommonEnum.QueryType.EDIT.getStatus().equals(queryType)){
            if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == businessAttr.getHasEditVisible())
                return false;
        }else if(CommonEnum.QueryType.VIEW.getStatus().equals(queryType)){
            if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == businessAttr.getHasViewVisible())
                return false;
        }

        return true;
    }

    /**
     * 根据属性归类封装业务申请表单数据结构（易用）
     * @param businessAttrList
     * @return
     */
    public static List<Map<String, Object>> packageApplyAttrCfgList(List<BusinessAttrCfgVO> businessAttrList, Map<String, Object> businessAttrValMap, String queryType){
        if(businessAttrList == null || businessAttrList.isEmpty()) return null;

        List<Map<String, Object>> fromAttrList = new ArrayList<>();
        businessAttrList.forEach(businessAttr -> {
            if(!BusinessCfgUtil.hasAssignTypeAttCfg(businessAttr, queryType)){
                return;
            }

            Map<String, Object> classifyMap =
                    fromAttrList.stream()
                            .filter(classify -> businessAttr.getClassifyName().equals(classify.get("classifyName").toString()))
                            .findAny().orElse(null);
            if(classifyMap == null){
                classifyMap = new HashMap<>();
                classifyMap.put("classifyName", businessAttr.getClassifyName());
                classifyMap.put("attrList", new ArrayList<>());
                fromAttrList.add(classifyMap);
            }

            List<BusinessAttrCfgVO> attrList = (List<BusinessAttrCfgVO>)classifyMap.get("attrList");

            //判断是否是封闭式值,如果是则将关联的封闭式值封装值属性字段对象
            if(businessAttr.getHasClosedType().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()){
                //某些字段虽然是封闭式取值，但是值列表是由前端提供，比如地址
                if(businessAttrValMap.containsKey(businessAttr.getBusinessAttrId().toString())){
                    Map<String, Object> attrValObj = (Map<String, Object>)businessAttrValMap.get(businessAttr.getBusinessAttrId().toString());
                    businessAttr.setAttrValObj(attrValObj);
                }
            }

            attrList.add(businessAttr);
        });
        return fromAttrList;
    }

    /**
     * 根据表单属性归类封装对象给属性填值
     * @param applyDetailsCfg
     * @param details
     * @return
     */
    public static void packageApplyDetailsCfg(List<Map<String, Object>> applyDetailsCfg, Map<String, Object> details){
        if(applyDetailsCfg == null || applyDetailsCfg.isEmpty() || details == null || details.isEmpty()) {
            return;
        }

        //对授信项目数据做了特殊处理
        List<Map<String, Object>> itemList = details.containsKey("item") ? (List<Map<String, Object>>)details.get("item") : null;

        applyDetailsCfg.forEach(classifyMap -> {
            List<BusinessAttrCfgVO> attrList = (List<BusinessAttrCfgVO>)classifyMap.get("attrList");

            //将非项目分组对象填值
            attrList.stream().filter(attr -> !"item".equals(attr.getColumnGroup())).forEach(attr -> {
                BusinessCfgUtil.setBusinessAttrColumnVal(attr,details);
            });

            //判断是否有项目数据（一般授信申请数据才有项目数据）
            if(itemList != null && itemList.size() >0){
                //判断是否有项目字段
                List<BusinessAttrCfgVO> itemAttrList = attrList.stream().filter(attr -> "item".equals(attr.getColumnGroup())).collect(Collectors.toList());
                if(itemAttrList == null || itemAttrList.isEmpty()){
                    return;
                }

                BusinessAttrCfgVO temp = null;
                //由于项目字段配置只有一组，但是项目数据有多条，所以要额外生产多组项目字段配置
                List<BusinessAttrCfgVO> newItemAttrList = new ArrayList<>();
                for(int i = 0;i<itemList.size();i++){
                    Map<String, Object> item = itemList.get(i);

                    //通过系统项目字段配置，来生成额外的项目字段配置，第一条项目数据填值到系统项目字段配置中
                    for(BusinessAttrCfgVO itemAttr:itemAttrList){
                        if(i == 0){
                            temp = itemAttr;
                            temp.setProjectId((Long)item.get("id"));
                        }else{
                            temp = BusinessAttrCfgVO.newInitInstance(
                                    itemAttr.getBusinessAttrId(),itemAttr.getColumnName(),itemAttr.getColumnCh(),itemAttr.getColumnType(),itemAttr.getColumnGroup(),
                                    itemAttr.getClassifyName(),itemAttr.getDefaultValueType(),itemAttr.getDefaultValue(),(Long)item.get("id")
                            );
                            temp.setAttrValObj(itemAttr.getAttrValObj());
                            temp.setHasClosedType(itemAttr.getHasClosedType());
                        }

                        //对字段进行填值
                        BusinessCfgUtil.setBusinessAttrColumnVal(temp,item);

                        //大于第一条项目数据填入额外生成的项目字段配置，并放入临时数组，结束生成后通过原系统项目字段配置最后一个对象的下标，加入到其后面
                        if(i > 0){
                            newItemAttrList.add(temp);
                        }
                    }
                }

                //通过原系统项目字段配置最后一个对象的下标，加入到其后面
                int idx = attrList.indexOf(itemAttrList.get(itemAttrList.size() - 1));
                attrList.addAll(idx+1,newItemAttrList);
            }
        });
    }

    /**
     * 将数据填入属性配置对象的字段值
     * @param attr
     * @param data
     */
    public static void setBusinessAttrColumnVal(BusinessAttrCfgVO attr, Map<String, Object> data){
        //判断是否是封闭式值,如果是则将关联的封闭式值封装值属性字段对象
        if(attr.getHasClosedType().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue() && attr.getAttrValObj() != null){
            Map<String, BusinessAttrVal> attrValMap = (Map<String, BusinessAttrVal>)attr.getAttrValObj().get("attrValMap");
            if(data.get(attr.getColumnName()) != null){
                BusinessAttrVal attrVal = attrValMap.get(StringUtil.objToString(data.get(attr.getColumnName())));
                if(attrVal != null){
                    attr.setColumnVal(attrVal.getId().toString());
                }
            }
        }else{
            attr.setColumnVal(StringUtil.objToString(data.get(attr.getColumnName())));
        }
    }

    /**
     * 筛选出集合内指定的业务属性字段
     * @param columnName PS：columnName为封装至redis的BusinessAttrCfgVO对象里的columnName
     * @param businessAttrList
     * @return
     */
    public static BusinessAttrCfgVO getBusinessAttrByColumnName(String columnName, List<BusinessAttrCfgVO> businessAttrList){
        if(StringUtil.isEmpty(columnName) || businessAttrList == null || businessAttrList.isEmpty()) return null;

        return businessAttrList.stream().filter(attr -> attr.getColumnName().equals(columnName)).findAny().orElse(null);
    }

    /**
     * 筛选出集合内指定的业务属性字段集合
     * @param tableName
     * @param businessAttrList
     * @return
     */
    public static List<BusinessAttrCfgVO> getBusinessAttrListByTableName(String tableName, List<BusinessAttrCfgVO> businessAttrList){
        if(StringUtil.isEmpty(tableName) || businessAttrList == null || businessAttrList.isEmpty()) return null;

        return businessAttrList.stream()
                        .filter(attr -> attr.getColumnName().startsWith(tableName))
                        .collect(Collectors.toList());
    }

    /**
     * 根据公司和类型查询业务属性字段集合
     * @param companyId
     * @param typeId
     * @param status
     * @return
     */
    public static List<BusinessAttrCfgVO> getBusinessAttrCfgVOList(Long companyId,Long typeId,Integer status){
        //设置返回结果集
        List<BusinessAttrCfgVO> businessAttrCfgVOList = new ArrayList<>();
        if (companyId == null || typeId == null)
            return businessAttrCfgVOList;
        Map<String, List<BusinessAttrCfgVO>> mapByTypeId = (Map<String, List<BusinessAttrCfgVO>>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR,companyId.toString());

        if (typeId != null) { //业务id不为空时取对应业务id的
            List<BusinessAttrCfgVO> list = mapByTypeId.get(typeId.toString());
            if (list == null)
                return businessAttrCfgVOList;
            businessAttrCfgVOList.addAll(list);
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                businessAttrCfgVOList.addAll(mapByTypeId.get(typeIdStr));
            }
        }
        //做状态过滤
        if (!businessAttrCfgVOList.isEmpty() && status != null) {
            businessAttrCfgVOList = businessAttrCfgVOList.stream().filter(businessAttrCfgVO -> {
                return  status == businessAttrCfgVO.getStatus().intValue();
            }).collect(Collectors.toList());
        }
        return businessAttrCfgVOList;
    }

    //翻译配置字段的值
    public static String getBusinessAttrVal(Long valId,Long companyId,Long typeId,Long attrId){
        if (StringUtil.isEmpty(valId)) return null;
        Map<String, Map> mapByTypeId =  (Map<String, Map>)redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,companyId.toString());
        if (mapByTypeId == null) return null;
        Map<String,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeId.toString());
        if (mapByAttrId == null) return null;
        String val = null;
        if (attrId == null) {
            for (Object tmpAttrId:mapByAttrId.keySet()) {
                List<BusinessAttrVal> businessAttrValList = mapByAttrId.get(tmpAttrId);
                if (businessAttrValList == null) continue;
                businessAttrValList = businessAttrValList.stream().filter(businessAttrVal -> businessAttrVal.getId().longValue() == valId).collect(Collectors.toList());
                if (ListUtil.isEmpty(businessAttrValList)) continue;
                val = businessAttrValList.get(0).getValueCh();break;
            }
        }else {
            List<BusinessAttrVal> businessAttrValList = mapByAttrId.get(attrId.toString());
            if (ListUtil.isEmpty(businessAttrValList)) return null;
            businessAttrValList = businessAttrValList.stream().filter(businessAttrVal -> businessAttrVal.getId().longValue() == valId).collect(Collectors.toList());
            if (ListUtil.isEmpty(businessAttrValList)) return null;
            val = businessAttrValList.get(0).getValueCh();
        }
        return val;
    }



    //翻译配置字段的值
    public static String getBusinessAttrVal(Long valId){
        if (StringUtil.isEmpty(valId)) return null;
        Map<Object, Object> mapByCompanyId =  redisUtil.hmget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL);
        if (mapByCompanyId == null) return null;
        String val = "";
        outer:
        for (Object companyId:mapByCompanyId.keySet()) {
            Map<Object,Map>  mapByTypeId = (Map<Object, Map>) mapByCompanyId.get(companyId);
            if (mapByTypeId == null) continue;
            for (Object typeId:mapByTypeId.keySet()) {
                Map<Object,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeId.toString());
                if (mapByAttrId == null) continue;
                for (Object attrId:mapByAttrId.keySet()) {
                    List<BusinessAttrVal> businessAttrValList = mapByAttrId.get(attrId);
                    if (businessAttrValList == null) continue;
                    businessAttrValList = businessAttrValList.stream().filter(businessAttrVal -> businessAttrVal.getId().longValue() == valId).collect(Collectors.toList());
                    if (ListUtil.isEmpty(businessAttrValList)) continue;
                    val = businessAttrValList.get(0).getValueCh();
                    break outer;
                }
            }
        }
        return val;
    }

    /**
     * 读取redis 获取字段取值范围
     * 筛选层级companyId>typeId>attrId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * attrId为空时取typeId的全部
     * status为空时不做状态筛选
     */
    public static Map<String,List<BusinessAttrVal>> getBusinessAttrValList(Long companyId,Long typeId,Integer status) { ;
        //设置返回结果集
        Map<String,List<BusinessAttrVal>> businessAttrValMap = new HashMap<>();
        if (companyId == null)
            return businessAttrValMap;
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,companyId.toString());
        if (mapByTypeId == null) {
            return businessAttrValMap;
        }

        if (typeId == null) { //业务id不为空时取对应业务id的
            return businessAttrValMap;
        }

        Map<String,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeId.toString());
        if (mapByAttrId == null)
            return businessAttrValMap;
        businessAttrValMap.putAll(mapByAttrId);
        //做状态过滤
        if (!businessAttrValMap.isEmpty() && status != null) {
            Set<String> keySet = businessAttrValMap.keySet();
            for (String key: keySet) {
                businessAttrValMap.put(key, businessAttrValMap.get(key).stream().filter(businessAttrVal -> {
                    return  status == businessAttrVal.getStatus().intValue();
                }).collect(Collectors.toList()) );
            }
        }
        return  businessAttrValMap;
    }


    /**
     * 读取redis 获取字段取值范围
     * 筛选层级companyId>typeId>attrId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * attrId为空时取typeId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessAttrVal> getBusinessAttrValList(Long companyId,Long typeId,Long attrId,Integer status) { ;
        //设置返回结果集
        List<BusinessAttrVal> businessAttrValList = new ArrayList<>();
        if (companyId == null)
            return businessAttrValList;
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,companyId.toString());
        if (mapByTypeId == null)
            return businessAttrValList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            Map<String,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeId.toString());
            if (mapByAttrId == null)
                return businessAttrValList;
            if (attrId != null){  //字段id不为空时取对应字段的
                List<BusinessAttrVal> list = mapByAttrId.get(attrId.toString());
                if (list == null)
                    return businessAttrValList;
                businessAttrValList.addAll(list);
            }else { //字段id为空时取业务id下所有字段的
                for (String attrIdStr:mapByAttrId.keySet()) {
                   businessAttrValList.addAll(mapByAttrId.get(attrIdStr));
                }
            }
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                Map<String,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeIdStr.toString());
                if (mapByAttrId != null) {
                    for (String attrIdStr:mapByAttrId.keySet()) {
                        businessAttrValList.addAll(mapByAttrId.get(attrIdStr));
                    }
                }
            }
        }
        //做状态过滤
        if (!businessAttrValList.isEmpty() && status != null) {
            businessAttrValList = businessAttrValList.stream().filter(businessAttrVal -> {
                return  status == businessAttrVal.getStatus().intValue();
            }).collect(Collectors.toList());
        }
        return  businessAttrValList;
    }

    /**
     * 读取redis 获取字段费用计算公式
     * 筛选层级companyId>typeId>attrId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * attrId为空时取typeId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessFeeFormula> getBusinessFeeFormulaList(Long companyId, Long typeId, Long attrId, Integer status) { ;
        //设置返回结果集
        List<BusinessFeeFormula> businessFeeFormulaList = new ArrayList<>();
        if (companyId == null)
            return businessFeeFormulaList;
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_FEE_FORMULA,companyId.toString());
        if (mapByTypeId == null)
            return businessFeeFormulaList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            Map<String,List<BusinessFeeFormula>> mapByAttrId = mapByTypeId.get(typeId.toString());
            if (mapByAttrId == null)
                return businessFeeFormulaList;
            if (attrId != null){  //字段id不为空时取对应字段的
                List<BusinessFeeFormula> list = mapByAttrId.get(attrId.toString());
                if (list == null)
                    return businessFeeFormulaList;
                businessFeeFormulaList.addAll(list);
            }else { //字段id为空时取业务id下所有字段的
                for (String attrIdStr:mapByAttrId.keySet()) {
                    businessFeeFormulaList.addAll(mapByAttrId.get(attrIdStr));
                }
            }
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                Map<String,List<BusinessFeeFormula>> mapByAttrId = mapByTypeId.get(typeIdStr.toString());
                if (mapByAttrId != null) {
                    for (String attrIdStr:mapByAttrId.keySet()) {
                        businessFeeFormulaList.addAll(mapByAttrId.get(attrIdStr));
                    }
                }
            }
        }
        //做状态过滤
        if (!businessFeeFormulaList.isEmpty() && status != null) {
            businessFeeFormulaList = businessFeeFormulaList.stream().filter(businessFeeFormula -> {
                return  (status == businessFeeFormula.getStatus().intValue());
            }).collect(Collectors.toList());
        }
        return  businessFeeFormulaList;
    }

     /**
     * 读取redis 获取流程状态
     * 筛选层级companyId>typeId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessFlow> getBusinessFlowList(Long companyId, Long typeId, Integer status) { ;
        //设置返回结果集
        List<BusinessFlow> businessFlowList = new ArrayList<>();
        if (companyId == null)
            return businessFlowList;
        //取缓存
        Map<String,List<BusinessFlow>>  mapByTypeId = (Map<String, List<BusinessFlow>>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_FLOW,companyId.toString());
        if (mapByTypeId == null)
            return businessFlowList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            List<BusinessFlow> list = mapByTypeId.get(typeId.toString());
            if (list == null)
                return businessFlowList;
            businessFlowList.addAll(list);
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                businessFlowList.addAll(mapByTypeId.get(typeIdStr));
            }
        }
        //做状态过滤
        if (!businessFlowList.isEmpty() && status != null) {
            businessFlowList = businessFlowList.stream().filter(businessFlow -> {
                return  (status == businessFlow.getStatus().intValue());
            }).collect(Collectors.toList());
        }
        return  businessFlowList;
    }

    /**
     * 读取redis 获取一种业务的所有流程状态
     * typeId不能为空
     * status为空时不做状态筛选
     */
    public static List<BusinessFlow> getBusinessFlowList( Long typeId, Integer status) { ;

       //设置返回结果集
        List<BusinessFlow> businessFlowList = new ArrayList<>();
        if (typeId == null) return businessFlowList;

        //取所有流程的缓存
        Map<Object,Object>  mapByCompanyId = redisUtil.hmget(RedisConstant.REDIS_KEY_BUSINESS_FLOW);
        if (mapByCompanyId == null) return businessFlowList;

        Map<String,List<BusinessFlow>> mapByTypeId = new HashMap<>();
        for (Object companyId:mapByCompanyId.keySet()) {
            mapByTypeId = (Map<String, List<BusinessFlow>>) mapByCompanyId.get(companyId);
            for (String typeIdStr:mapByTypeId.keySet()) {
                if (typeIdStr.equals(typeId.toString())) {
                    businessFlowList = mapByTypeId.get(typeIdStr);
                    break;
                }
            }
        }

        //做状态过滤
        if (!businessFlowList.isEmpty() && status != null) {
            businessFlowList = businessFlowList.stream().filter(businessFlow ->
                status == businessFlow.getStatus().intValue()
            ).collect(Collectors.toList());
        }
        return  businessFlowList;
    }


    /**
     * 根据流程CODE 筛选出指定流程对象
     * @param flowCode
     * @param businessFlowList
     * @return
     */
    public static BusinessFlow getBusinessFlowByFlowCode(String flowCode, List<BusinessFlow> businessFlowList){
        if(StringUtil.isEmpty(flowCode) || businessFlowList == null || businessFlowList.isEmpty()) return null;

        return businessFlowList.stream().filter(businessFlow -> businessFlow.getFlowCode().equals(flowCode)).findAny().orElse(null);
    }


    /**
     * 读取redis 获取流转权限
     * 筛选层级companyId>typeId>flowId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * flowId为空时取typeId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessFlowPermit> getBusinessFlowPermitList(Long companyId, Long typeId, Long flowId, Integer status) {
        //设置返回结果集
        List<BusinessFlowPermit> businessFlowPermitList = new ArrayList<>();
        if (companyId == null)
            return businessFlowPermitList;
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_FLOW_PERMIT,companyId.toString());
        if (mapByTypeId == null)
            return businessFlowPermitList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            Map<String,List<BusinessFlowPermit>> mapByFlowId = mapByTypeId.get(typeId.toString());
            if (mapByFlowId == null)
                return businessFlowPermitList;
            if (flowId != null){  //流转id不为空时取对应流转的
                List<BusinessFlowPermit> list = mapByFlowId.get(flowId.toString());
                if (list == null)
                    return businessFlowPermitList;
                businessFlowPermitList.addAll(list);
            }else { //流转id为空时取业务id下所有字段的
                for (String flowIdStr:mapByFlowId.keySet()) {
                    businessFlowPermitList.addAll(mapByFlowId.get(flowIdStr));
                }
            }
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                Map<String,List<BusinessFlowPermit>> mapByFlowId = mapByTypeId.get(typeIdStr.toString());
                if (mapByFlowId != null) {
                    for (String flowIdStr:mapByFlowId.keySet()) {
                        businessFlowPermitList.addAll(mapByFlowId.get(flowIdStr));
                    }
                }
            }
        }
        //做状态过滤
        if (!businessFlowPermitList.isEmpty() && status != null) {
            businessFlowPermitList = businessFlowPermitList.stream().filter(businessFlowPermit -> {
                return  (status == businessFlowPermit.getStatus().intValue());
            }).collect(Collectors.toList());
        }
        return  businessFlowPermitList;
    }


    /**
     * 读取redis 根据状态获取业务类型
     */
    public static List<BusinessType> getBusinessTypeList(Long companyId,Integer status) {
        if (companyId == null)
            return Arrays.asList();
        List<BusinessType> businessTypeList = (List<BusinessType>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_TYPE,companyId.toString());
        if (businessTypeList != null && !businessTypeList.isEmpty() && status != null) {
            businessTypeList = businessTypeList.stream().filter(businessType -> {
                return  (status == businessType.getStatus());
            }).collect(Collectors.toList());
        }
        return businessTypeList;
    }

    /**
     * 根据业务类型ID读取
     * @param companyId
     * @param businessTypeId
     * @return
     */
    public static BusinessType getBusinessType(Long companyId, Long businessTypeId){
        if(companyId == null){
            return null;
        }

        Object o = redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_TYPE,companyId.toString());
        if(o == null){
            return null;
        }
        List<BusinessType> businessTypeList = (List<BusinessType>) o;
        return businessTypeList.stream().filter(businessType -> businessType.getId().longValue() == businessTypeId.longValue()).findAny().orElse(null);
    }

    /**
     * 根据业务类型名称读取
     * @param companyId
     * @param businessName
     * @return
     */
    public static BusinessType getBusinessType(Long companyId, String businessName){
        if(companyId == null){
            return null;
        }

        Object o = redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_TYPE,companyId.toString());
        if(o == null){
            return null;
        }
        List<BusinessType> businessTypeList = (List<BusinessType>) o;
        return businessTypeList.stream().filter(businessType -> businessType.getBusinessName().equals(businessName)).findAny().orElse(null);
    }


    /**
     * 读取redis 获取流程流转
     * 筛选层级companyId>typeId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessWorkFlow> getBusinessWorkFlowList(Long companyId, Long typeId, Integer status) {
        //设置返回结果集
        List<BusinessWorkFlow> businessWorkFlowList = new ArrayList<>();
        if (companyId == null)
            return businessWorkFlowList;
        //取缓存
        Map<String,List<BusinessWorkFlow>>  mapByTypeId = (Map<String, List<BusinessWorkFlow>>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW,companyId.toString());
        if (mapByTypeId == null)
            return businessWorkFlowList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            List<BusinessWorkFlow> list = mapByTypeId.get(typeId.toString());
            if (list == null)
                return businessWorkFlowList;
            businessWorkFlowList.addAll(list);
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                businessWorkFlowList.addAll(mapByTypeId.get(typeIdStr));
            }
        }
        //做状态过滤
        if (!businessWorkFlowList.isEmpty() && status != null) {
            businessWorkFlowList = businessWorkFlowList.stream().filter(businessWorkFlow -> {
                return  (status == businessWorkFlow.getStatus().intValue());
            }).collect(Collectors.toList());
        }
        return  businessWorkFlowList;
    }

    /**
     * 读取redis 获取单种业务的所有流程流转
     * typeId不能为空
     * status为空时不做状态筛选
     */
    public static List<BusinessWorkFlow> getBusinessWorkFlowList( Long typeId, Integer status) {
        //设置返回结果集
        List<BusinessWorkFlow> businessWorkFlowList = new ArrayList<>();
        if (typeId == null)
            return businessWorkFlowList;
        //取缓存
        Map<Object, Object> mapByCompanyId =  redisUtil.hmget(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW);
        if (mapByCompanyId == null)
            return businessWorkFlowList;

        Map<String,List<BusinessWorkFlow>> mapByTypeId = new HashMap<>();
        for (Object companyId:mapByCompanyId.keySet()) {
            mapByTypeId = (Map<String, List<BusinessWorkFlow>>) mapByCompanyId.get(companyId);
            for (String typeIdStr:mapByTypeId.keySet()) {
                if (typeIdStr.equals(typeId.toString())) {
                    businessWorkFlowList = mapByTypeId.get(typeIdStr);
                    break;
                }
            }
        }
        //做状态过滤
        if (!businessWorkFlowList.isEmpty() && status != null) {
            businessWorkFlowList = businessWorkFlowList.stream().filter(businessWorkFlow ->
                    status == businessWorkFlow.getStatus().intValue()
            ).collect(Collectors.toList());
        }
        return  businessWorkFlowList;
    }

    /**
     * 根据前后流程状态查询流程扭转配置对象
     * @param companyId
     * @param typeId
     * @param status
     * @param beforeFlow
     * @param afterFlow
     * @return
     */
    public static BusinessWorkFlow getBusinessWorkFlow(Long companyId, Long typeId, Integer status, String beforeFlow, String afterFlow){
        List<BusinessWorkFlow> workFlowList = getBusinessWorkFlowList(companyId,typeId,status);
        return workFlowList.stream().filter(workFlow -> workFlow.getBeforeFlow().equals(beforeFlow) && workFlow.getAfterFlow().equals(afterFlow)).findAny().orElse(null);
    }

    /**
     * 读取redis 获取流程流转以及流转条件和字段
     * 筛选层级companyId>typeId
     * companyId不能为空
     * typeId为空时取companyId的全部
     */
    public static List<BusinessWorkFlowExtendVO> getBusinessWorkFlowExtendList(Long companyId, Long typeId) {
        //设置返回结果集
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOList = new ArrayList<>();
        if (companyId == null)
            return businessWorkFlowExtendVOList;
        //取缓存
        Map<String,List<BusinessWorkFlowExtendVO>>  mapByTypeId = (Map<String, List<BusinessWorkFlowExtendVO>>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW_EXTEND,companyId.toString());
        if (mapByTypeId == null)
            return businessWorkFlowExtendVOList;
        if (typeId != null) { //业务id不为空时取对应业务id的
            List<BusinessWorkFlowExtendVO> list = mapByTypeId.get(typeId.toString());
            if (list == null)
                return businessWorkFlowExtendVOList;
            businessWorkFlowExtendVOList.addAll(list);
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                businessWorkFlowExtendVOList.addAll(mapByTypeId.get(typeIdStr));
            }
        }
        return  businessWorkFlowExtendVOList;
    }

    /**
     * 读取redis 获取流程流转以及流转条件和字段
     * 筛选层级companyId>typeId
     * companyId不能为空
     * typeId为空时取companyId的全部
     */
    public static List<BusinessWorkFlowExtendVO> getBusinessWorkFlowExtendList(Long typeId) {
        //设置返回结果集
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOList = new ArrayList<>();
        if (typeId == null)
            return businessWorkFlowExtendVOList;
        //取缓存
        Map<Object, Object> mapByCompanyId = redisUtil.hmget(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW_EXTEND);
        if (mapByCompanyId == null)
            return businessWorkFlowExtendVOList;

        Map<String,List<BusinessWorkFlowExtendVO>> mapByTypeId = new HashMap<>();
        for (Object companyId:mapByCompanyId.keySet()) {
            mapByTypeId = (Map<String, List<BusinessWorkFlowExtendVO>>) mapByCompanyId.get(companyId);
            for (String typeIdStr:mapByTypeId.keySet()) {
                if (typeIdStr.equals(typeId.toString())) {
                    businessWorkFlowExtendVOList = mapByTypeId.get(typeIdStr);
                    break;
                }
            }
        }
        return  businessWorkFlowExtendVOList;
    }

    /**
     * 根据流程CODE筛选出需要的流程扭转配置
     * @param flowCode
     * @param businessWorkFlowExtendList
     * @return
     */
    public static List<BusinessWorkFlowExtendVO> getBusinessWorkFlowExtendListByFlowCode(String flowCode, List<BusinessWorkFlowExtendVO> businessWorkFlowExtendList){
        if(StringUtil.isEmpty(flowCode) || businessWorkFlowExtendList == null || businessWorkFlowExtendList.isEmpty()) return null;

        return businessWorkFlowExtendList.stream().filter(workFlowCfg -> workFlowCfg.getBeforeFlow().equals(flowCode)).collect(Collectors.toList());
    }

    /**
     * 根据流程CODE与下一个流程筛选出需要的流程扭转配置
     * @param flowCode
     * @param businessWorkFlowExtendList
     * @return
     */
    public static BusinessWorkFlowExtendVO getBusinessWorkFlowExtend(String flowCode, String nextFlowCode, List<BusinessWorkFlowExtendVO> businessWorkFlowExtendList){
        if(StringUtil.isEmpty(flowCode) || StringUtil.isEmpty(nextFlowCode) || businessWorkFlowExtendList == null || businessWorkFlowExtendList.isEmpty()) return null;

        return businessWorkFlowExtendList
                .stream()
                .filter(workFlowCfg -> workFlowCfg.getBeforeFlow().equals(flowCode) && workFlowCfg.getAfterFlow().equals(nextFlowCode))
                .findAny().orElse(null);
    }

    /**
     * 根据流程扭转ID获取流程扭转配置
     * @param workFlowId
     * @param companyId
     * @param typeId
     * @return
     */
    public static BusinessWorkFlowExtendVO getBusinessWorkFlowExtend(Long workFlowId, Long companyId, Long typeId){
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOList = getBusinessWorkFlowExtendList(companyId,typeId);

        return businessWorkFlowExtendVOList.stream().filter(vo -> vo.getId().longValue() == workFlowId.longValue()).findAny().orElse(null);
    }

    /**
     * 读取redis 获取流转流程属性
     * 筛选层级companyId>typeId>flowId
     * companyId不能为空
     * typeId为空时取companyId的全部
     * flowId为空时取typeId的全部
     * status为空时不做状态筛选
     */
    public static List<BusinessWorkFlowAttr> getBusinessWorkFlowAttrList(Long companyId, Long typeId, Long flowId, Integer status) {
        //设置返回结果集
        List<BusinessWorkFlowAttr> businessWorkFlowAttrList = new ArrayList<>();
        if (companyId == null)
            return businessWorkFlowAttrList;
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_WORK_FLOW_ATTR,companyId.toString());
        if (mapByTypeId == null)
            return businessWorkFlowAttrList;

        if (typeId != null) { //业务id不为空时取对应业务id的
            Map<String,List<BusinessWorkFlowAttr>> mapByFlowId = mapByTypeId.get(typeId.toString());
            if (mapByFlowId == null)
                return businessWorkFlowAttrList;
            if (flowId != null){  //流转id不为空时取对应流转的
                List<BusinessWorkFlowAttr> list = mapByFlowId.get(flowId.toString());
                if (list == null)
                    return businessWorkFlowAttrList;
                businessWorkFlowAttrList.addAll(list);
            }else { //流转id为空时取业务id下所有字段的
                for (String flowIdStr:mapByFlowId.keySet()) {
                    businessWorkFlowAttrList.addAll(mapByFlowId.get(flowIdStr));
                }
            }
        }else { //业务类型为空时取公司下所有业务类型的
            for (String typeIdStr:mapByTypeId.keySet()) {
                Map<String,List<BusinessWorkFlowAttr>> mapByFlowId = mapByTypeId.get(typeIdStr.toString());
                if (mapByFlowId != null) {
                    for (String flowIdStr:mapByFlowId.keySet()) {
                        businessWorkFlowAttrList.addAll(mapByFlowId.get(flowIdStr));
                    }
                }
            }
        }
        //做状态过滤
        if (!businessWorkFlowAttrList.isEmpty() && status != null) {
            businessWorkFlowAttrList = businessWorkFlowAttrList.stream().filter(businessWorkFlowAttr -> {
                return  (status == businessWorkFlowAttr.getStatus().intValue());
            }).collect(Collectors.toList());
        }
        return  businessWorkFlowAttrList;
    }

    /**
     * 校验指定类（表）的字段
     * @param tableName
     * @param o
     * @param businessAttrList
     * @return
     */
    public static Result validBusinessAttr(String tableName, Object o, List<BusinessAttrCfgVO> businessAttrList, String validType){
        if(StringUtil.isEmpty(tableName) || o == null) Result.failure("缺少校验参数");
        if(businessAttrList == null || businessAttrList.isEmpty()) Result.failure("缺少业务字段配置");

        //筛选出需要用的业务字段配置数据
        List<BusinessAttrCfgVO> tempAttrList =
                businessAttrList.stream()
                                .filter(attr -> attr.getColumnName().startsWith(tableName))
                                .collect(Collectors.toList());

        Result result = Result.success();
        Class c = o.getClass();
        for (BusinessAttrCfgVO attr:tempAttrList) {
            String[] attrNameList = attr.getColumnName().split("\\_");
            try {
                Field field = c.getDeclaredField(attrNameList[1]);
                if(field == null) {
                    return Result.failure("字段【"+attr.getColumnCh()+"】不存在");
                }

                field.setAccessible(true);
                Result validRet = validAttr(field.get(o),attr, null,StringUtil.isEmpty(validType) ? CommonEnum.QueryType.AUDIT.getStatus():validType);
                if(validRet.getCode() == Result.RESULT_CODE_FAILURE){
                    return validRet;
                }
            } catch (Exception e) {
                logger.error("验证业务数据异常：",e);
                result.setCode(Result.RESULT_CODE_EXCEPTION);
                result.setMessage("验证业务数据异常："+e.getMessage());
                return result;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("123", "45634", "7892", "abch", "sdfhrthj", "mvkd");
        List<String> list2 = Arrays.asList("123", "456341");
        System.out.println(list.containsAll(list2));
    }

    /**
     * 校验字段
     * @param val
     * @param validAttr
     * @return
     */
    public static Result validAttr(Object val, BusinessAttrCfgVO validAttr, String preAttrVal,String validType){
        if(validAttr == null) return Result.failure("业务校验对象为NULL");

        String columnCh = null;
        if(StringUtil.isEmpty(validAttr.getColumnCh())){
            if(!StringUtil.isEmpty(validAttr.getRemark()) && validAttr.getRemark().lastIndexOf("-") != -1){
                columnCh = validAttr.getRemark().substring(validAttr.getRemark().lastIndexOf("-"),validAttr.getRemark().length()-1);
            }else{
                columnCh = validAttr.getRemark();
            }
        }else{
            columnCh = validAttr.getRemark();
        }

        //创建页面允许保存草稿，草稿可不校验必填
        if(!CommonEnum.QueryType.CREATE.getStatus().equals(validType)
                && validAttr.getRequired().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()
                && StringUtil.isEmpty(val)){
            if(validAttr.getPreColumnId() != null ){
                if(!StringUtil.isEmpty(validAttr.getPreColumnValue()) && validAttr.getPreColumnValue().equals(preAttrVal)){
                    return Result.failure("【"+validAttr.getClassifyName()+"】-->【"+columnCh+"】不能为空");
                }
            }else{
                return Result.failure("【"+validAttr.getClassifyName()+"】-->【"+columnCh+"】不能为空");
            }
        }

        //如果字段是身份证类型，做特殊校验
        if("id-card".equals(validAttr.getColumnType())){
            if(!StringUtil.isEmpty(val) && !StringUtil.identityCodeValid(val.toString())){
                return Result.failure("【"+validAttr.getClassifyName()+"】-->【"+columnCh+"】格式不正确");
            }
        }else{
            if(!StringUtil.isEmpty(val)
                    && !StringUtil.isEmpty(validAttr.getVerifyFormula())
                    && !StringUtil.regexValid(val.toString(),validAttr.getVerifyFormula()))
                return Result.failure("【"+validAttr.getClassifyName()+"】-->【"+columnCh+"】格式不正确");
        }

        return Result.success("验证通过");
    }

    /**
     * 审核字段校验
     * @param tableName
     * @param auditFormList
     * @param businessAttrList
     * @param validType
     * @return
     */
    public static Result validAttr(String tableName, List<AuditFormDTO> auditFormList, List<BusinessAttrCfgVO> businessAttrList, String validType){
//        if(StringUtil.isEmpty(tableName)) return Result.failure("缺少校验参数");
        if(auditFormList == null) return Result.failure("缺少校验参数");
        if(businessAttrList == null || businessAttrList.isEmpty()) Result.failure("缺少业务字段配置");

        //筛选出需要用的业务字段配置数据
        List<BusinessAttrCfgVO> tempAttrList = null;
        if(StringUtil.isEmpty(tableName)){
            tempAttrList = businessAttrList;
        }else{
            tempAttrList = businessAttrList.stream()
                    .filter(attr -> attr.getColumnName().startsWith(tableName))
                    .collect(Collectors.toList());
        }


        AuditFormDTO auditForm = null;
        for(BusinessAttrCfgVO attr:tempAttrList){
            //查询前置字段
            BusinessAttrCfgVO preColumn = null;
            if(attr.getPreColumnId() != null){
                preColumn = tempAttrList.stream().filter(vo -> vo.getBusinessAttrId().longValue() == attr.getPreColumnId()).findAny().orElse(null);
            }

            //如果有前置字段，则获取前置字段值
            String preColumnVal = null;
            final String preColumnName = preColumn == null ? null : preColumn.getColumnName();
            if(!StringUtil.isEmpty(preColumnName)){
                AuditFormDTO preForm = auditFormList.stream().filter( v -> preColumnName.equals(v.getColumnName())).findAny().orElse(null);
                if(preForm != null){
                    preColumnVal = preForm.getColumnValue();
                }
            }

            auditForm = auditFormList.stream().filter(auditFormDTO -> attr.getColumnName().equals(auditFormDTO.getColumnName())).findAny().orElse(null);
//            if(auditForm == null) {
//                return Result.failure("缺少【"+attr.getClassifyName()+"】-->【"+attr.getColumnCh()+"】");
//            }

            Result validRet = validAttr(auditForm == null?null:auditForm.getColumnValue(),attr, preColumnVal, StringUtil.isEmpty(validType) ? CommonEnum.QueryType.AUDIT.getStatus():validType);
            if(validRet.getCode() == Result.RESULT_CODE_FAILURE){
                return validRet;
            }
        }

        return Result.success();
    }

    /**
     * 校验指定单个字段
     * @param companyId
     * @param typeId
     * @param status
     * @param columnName PS：columnName为封装至redis的BusinessAttrCfgVO对象里的columnName
     * @param val
     * @param validType
     * @return
     */
    public static Result validAttr(Long companyId,Long typeId,Integer status,String columnName,Object val, String validType){
        List<BusinessAttrCfgVO>  businessAttrCfgVOList = getBusinessAttrCfgVOList(companyId,typeId,status);
        if (businessAttrCfgVOList == null || businessAttrCfgVOList.isEmpty())
            return Result.failure("未查询到业务校验对象");
        BusinessAttrCfgVO businessAttrCfgVO = getBusinessAttrByColumnName(columnName, businessAttrCfgVOList);
        return validAttr(val,businessAttrCfgVO, null,validType);
    }

    /**
     * 业务申请提交字段校验
     * @param companyId
     * @param typeId
     * @param formList
     * @return
     */
//    public static Result applyValidAttr(Long companyId,Long typeId, List<AuditFormDTO> formList){
//        List<BusinessAttrCfgVO> businessAttrCfgVOList = getBusinessAttrCfgVOList(companyId,typeId,CommonEnum.StatusEnnum.status1.getStatus());
//        //筛选出草稿/创建页面需传字段
//        List<BusinessAttrCfgVO> validAttrList =
//                businessAttrCfgVOList.stream().filter(validAttr -> {
//                        if(CommonEnum.WhetherEnum.NO.getStatus().intValue() == validAttr.getHasCreateNeed().intValue()){
//                            return false;
//                        }
//                        return true;
//                }).collect(Collectors.toList());
//
//        AuditFormDTO form = null;
//        Result result = null;
//        for(BusinessAttrCfgVO attrCfg:validAttrList){
//
//            //查询前置字段
//            BusinessAttrCfgVO preColumn = null;
//            if(attrCfg.getPreColumnId() != null){
//                preColumn = validAttrList.stream().filter(vo -> vo.getBusinessAttrId().longValue() == attrCfg.getPreColumnId()).findAny().orElse(null);
//            }
//
//            //如果有前置字段，则获取前置字段值
//            String preColumnVal = null;
//            final String preColumnName = preColumn == null ? null : preColumn.getColumnName();
//            if(!StringUtil.isEmpty(preColumnName)){
//                AuditFormDTO preForm = formList.stream().filter( v -> preColumnName.equals(v.getColumnName())).findAny().orElse(null);
//                if(preForm != null){
//                    preColumnVal = preForm.getColumnValue();
//                }
//            }

//            form = formList.stream().filter(f -> attrCfg.getColumnName().equals(f.getColumnName())).findAny().orElse(null);

//            result = validAttr(form == null?null:form.getColumnValue(),attrCfg, preColumnVal,CommonEnum.QueryType.AUDIT.getStatus());
//            if(result.getCode() != Result.RESULT_CODE_SUCCESS){
//                return result;
//            }
//        }
//        return Result.success();
//    }

    /**
     * 附件校验多个必传
     * @param fileList
     * @param workFlowFileList
     * @return
     */
    public static Result validFiles(List<File> fileList, List<BusinessWorkFlowFileVO> workFlowFileList){
        //无必传文件项，则返回成功
        if(workFlowFileList == null) return Result.success();
        if(fileList == null) return Result.failure("缺少必传文件");

        for(BusinessWorkFlowFileVO workFlowFile:workFlowFileList){
            Result result = validFile(fileList,workFlowFile);
            if(result.getCode() != Result.RESULT_CODE_SUCCESS) return result;
        }
        return Result.success();
    }

    /**
     * 附件校验必传
     * @param fileList
     * @param workFlowFile
     * @return
     */
    public static Result validFile(List<File> fileList, BusinessWorkFlowFileVO workFlowFile){
        if(workFlowFile == null) Result.failure("校验项为空");

        if(workFlowFile.getRequired().intValue() == CommonEnum.WhetherEnum.NO.getStatus().intValue()) {
            return Result.success();
        }

        List<File> temp = fileList.stream().filter(file -> workFlowFile.getFileCode().equals(file.getFileCode())).collect(Collectors.toList());
        if(temp.size() == 0) {
            return Result.failure("请上传【"+workFlowFile.getFileName()+"】文件");
        }

        if(workFlowFile.getUploadLimit() == null || workFlowFile.getUploadLimit().intValue() == 0) {
            return Result.success();
        }

        if(temp.size() > workFlowFile.getUploadLimit().intValue()) {
            return Result.failure("文件【"+workFlowFile.getFileName()+"】不能上传超过"+workFlowFile.getUploadLimit()+"个");
        }

        return Result.success();
    }

    /**
     * 根据公司id和业务类型名称获取业务类型id
     * @param companyId
     * @param typeName
     * @return
     */
    public static Long getBusinessTypeId(Long companyId,String typeName){
        List<BusinessType> businessTypeList =BusinessCfgUtil.getBusinessTypeList(companyId,CommonEnum.WhetherEnum.YES.getStatus());
        if (businessTypeList == null || businessTypeList.isEmpty())
            return null;
        businessTypeList = businessTypeList.stream().filter(businessType -> typeName.equals(businessType.getBusinessName())).collect(Collectors.toList());
        if (businessTypeList == null || businessTypeList.isEmpty())
            return null;
        return businessTypeList.get(0).getId();
    }


    /**
     * 获取用户对于某个业务有权限处理的流程
     * @param roleList
     * @param userId
     * @param companyId
     * @param typeId
     * @return
     */
    public static Result getPermitFlowCodeList(List<Role> roleList,Long userId, Long companyId, Long typeId){

        //查询业务类型对应的流程以及权限
        List<BusinessWorkFlowExtendVO> businessWorkFlowExtendVOList = BusinessCfgUtil.getBusinessWorkFlowExtendList(companyId,typeId);
        if (businessWorkFlowExtendVOList == null || businessWorkFlowExtendVOList.isEmpty())
            return Result.failure("无流程权限配置");

        //将角色list转为map
        Map<String,Boolean> roleTrueMap = new HashMap<>();
        roleList.stream().forEach(role -> {
            roleTrueMap.put(role.getId().toString(),true);
        });

        //开始筛选 登录人有权处理的流程
        businessWorkFlowExtendVOList = businessWorkFlowExtendVOList.stream().filter(businessWorkFlowExtendVO -> {
            //判断是否符合用户权限
            String[] userIdArray = businessWorkFlowExtendVO.getUserIds().split("\\,");
            for (String userIdStr:userIdArray) {
                if (userIdStr.equals(userId.toString()))
                    return true;
            }
            //判断是否符合角色权限
            String[] roleIdArray = businessWorkFlowExtendVO.getRoleIds().split("\\,");
            for (String roleId:roleIdArray) {
                if (roleTrueMap.get(roleId))
                    return true;
            }
            //同时没有角色权限和用户权限
            return false;
        }).collect(Collectors.toList());

        List<String> flowCodeList = businessWorkFlowExtendVOList.stream().map(businessWorkFlowExtendVO -> businessWorkFlowExtendVO.getBeforeFlow()).collect(Collectors.toList());
        return Result.success(flowCodeList);
    }

    /**
     * 获取流程扭转附件配置
     * @param companyId
     * @param typeId
     * @param status
     * @return
     */
    public static List<BusinessWorkFlowFileVO> getBusinessWorkFlowFileVOList(Long companyId, Long typeId, Long flowId,Integer status){
        if(companyId == null) return new ArrayList<>();

        List<BusinessWorkFlowFileVO> workFlowFileCfgList = new ArrayList<>();

        Map<String, List<BusinessWorkFlowFileVO>> workFlowFileCfgMap =
                (Map<String, List<BusinessWorkFlowFileVO>>)redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_WORKFLOW_FILE_CFG, companyId.toString());

        //为NULL返回全部流程扭转文件配置
        if(typeId == null) {
            Set<String> keys = workFlowFileCfgMap.keySet();
            List<BusinessWorkFlowFileVO> temp = new ArrayList<>();
            keys.forEach(key -> {
                temp.addAll(workFlowFileCfgMap.get(key).stream().filter(vo -> vo.getStatus().intValue() == status.intValue()).collect(Collectors.toList()));
            });
            workFlowFileCfgList = temp;
        }else{
            if(workFlowFileCfgMap.containsKey(typeId.toString())){
                workFlowFileCfgList = workFlowFileCfgMap.get(typeId.toString());
            }
        }

        if(flowId != null){
            workFlowFileCfgList = workFlowFileCfgList.stream().filter(vo -> vo.getWorkFlowId().intValue() == flowId.intValue()).collect(Collectors.toList());
        }

        return workFlowFileCfgList.stream().filter(vo -> vo.getStatus().intValue() == status.intValue()).collect(Collectors.toList());
    }

    /**
     * 获取指定公司业务类型指定流程的配置
     * @param companyId
     * @param typeId
     * @param flowCode
     * @return
     */
    public static Result<Map<String, Object>> getCurrentFlowCfg(Long companyId, Long typeId, String flowCode){

        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;
        //获取所有封闭式取值范围
        List<BusinessAttrVal> attrValList =
                BusinessCfgUtil.getBusinessAttrValList(companyId,typeId,null,status1.getStatus());
        Map<String, Object> data = new HashMap<>();
        data.put("attrValList", attrValList);

        //获取流程
        List<BusinessFlow> businessFlowList =
                BusinessCfgUtil.getBusinessFlowList(companyId,typeId, status1.getStatus());
        BusinessFlow businessFlow = BusinessCfgUtil.getBusinessFlowByFlowCode(flowCode, businessFlowList);
        if(businessFlow == null){
            return Result.failure("获取流程失败");
        }

        //完结流程 无流转配置
        if (businessFlow.getFlowType().intValue() == 11 || businessFlow.getFlowType().intValue() == 10)
            return Result.success(data);

        //获取流程扭转配置
        List<BusinessWorkFlowExtendVO> workFlowCfgList =
                BusinessCfgUtil.getBusinessWorkFlowExtendList(companyId,typeId);
        if(workFlowCfgList == null || workFlowCfgList.isEmpty()) return Result.failure("流程【"+businessFlow.getFlowName()+"】获取后续流程失败");
        //获取当前流程的所有可能扭转流程的配置
        List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg = BusinessCfgUtil.getBusinessWorkFlowExtendListByFlowCode(flowCode, workFlowCfgList);
        if(trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()) return  Result.failure("流程【"+businessFlow.getFlowName()+"】获取扭转配置失败");

        trailingWorkFlowCfg = BusinessCfgUtil.sortWorkFlowForAudit(trailingWorkFlowCfg,typeId,flowCode);

        data.put("trailingWorkFlowCfg", trailingWorkFlowCfg);

        return Result.success(data);
    }

    /**
     * 获取详情页面表单配置
     * @param companyId
     * @param typeId
     * @return
     */
    public static Result<Map<String, Object>> getDetailsCfg(Long companyId, Long typeId){
        CommonEnum.StatusEnnum status1 = CommonEnum.StatusEnnum.status1;
        BusinessType businessType = BusinessCfgUtil.getBusinessType(companyId,typeId);

        //获取详情表单属性配置
        List<BusinessAttrCfgVO> businessAttrList =
                BusinessCfgUtil.getBusinessAttrCfgVOList(companyId,typeId, status1.getStatus());
        if(businessAttrList == null || businessAttrList.isEmpty()) return Result.failure("当前业务【"+businessType.getBusinessName()+"】无业务表单配置数据");
        //将表单属性配置封装
        List<Map<String, Object>> formAttrList = BusinessCfgUtil.packageFormAttrList(businessAttrList, CommonEnum.QueryType.VIEW.getStatus());

        //获取详情附件配置
        List<BusinessFileCfg> formFileCfgList = BusinessCfgUtil.getBusinessFileCfgList(companyId,typeId,status1.getStatus());

        Map<String, Object> data = new HashMap<>();
        data.put("formAttrList", formAttrList);
        data.put("formFileCfgList", formFileCfgList);
        return Result.success(data);
    }

    /**
     * 给项目字段配置做特殊处理（根据单据时间的项目记录数量，生产对应数量的项目字段配置）
     * @param trailingWorkFlowCfg
     * @param details
     */
    public static void disposeItemCfg(List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg, Map<String, Object> details){
        if(details == null || trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()){
            return;
        }

        List<Map<String, Object>> itemList = (List<Map<String, Object>>)details.get("item");
        trailingWorkFlowCfg.forEach(workFlowCfg -> {
            if(workFlowCfg.getBusinessAttrCfgVOList() == null || workFlowCfg.getBusinessAttrCfgVOList().isEmpty()){
                return;
            }

            //处理非项目字段
            List<BusinessAttrCfgVO> attrCfgVOList = workFlowCfg.getBusinessAttrCfgVOList()
                    .stream()
                    .filter(attrCfg -> !"item".equals(attrCfg.getColumnGroup()))
                    .collect(Collectors.toList());
            if(attrCfgVOList != null && attrCfgVOList.size() > 0){
                attrCfgVOList.forEach(attrCfg -> {
                    //给字段配置加上对应的数据表ID
                    Object id = details.get(attrCfg.getColumnName().split("_")[0]+"_id");
                    attrCfg.setProjectId(id == null ? null:Long.parseLong(id.toString()));
                    if("column_value".equals(attrCfg.getDefaultValueType())){
                        Object defaultValue = details.get(attrCfg.getDefaultValue());
                        attrCfg.setDefaultValue(defaultValue == null ? null:defaultValue.toString());
                    }
                });
            }

            //处理项目字段
            List<BusinessAttrCfgVO> itemAttrList = workFlowCfg.getBusinessAttrCfgVOList()
                    .stream()
                    .filter(attrCfg -> "item".equals(attrCfg.getColumnGroup()))
                    .collect(Collectors.toList());
            //如果有项目配置字段，对其进行处理加工
            if(itemAttrList != null && itemAttrList.size() > 0 && itemList != null && !itemList.isEmpty()){
                //复制一份项目配置作为生成额外配置的模板
                List<BusinessAttrCfgVO> newTempList = ListUtil.copyTo(itemAttrList,BusinessAttrCfgVO.class);
                Long tempId = 0L;
                BusinessAttrCfgVO oneAttrCfg = itemAttrList.get(0);
                int index = workFlowCfg.getBusinessAttrCfgVOList().indexOf(oneAttrCfg);
                //给第一组项目字段添加授信项目名称字段配置
                workFlowCfg.getBusinessAttrCfgVOList()
                        .add(index,
                                BusinessAttrCfgVO.newInitInstance(tempId,"creditItem_itemName",
                                        "授信项目名称","text",oneAttrCfg.getColumnGroup(),
                                        oneAttrCfg.getClassifyName(),"fixed_value",
                                        itemList.get(0).get("creditItem_itemName").toString(),Long.parseLong(itemList.get(0).get("id").toString()))
                        )
                ;
                //给第一组项目字段配置都关联上项目ID
                for(BusinessAttrCfgVO v:itemAttrList){
                    v.setProjectId(Long.parseLong(itemList.get(0).get("id").toString()));
                    //读取默认值
                    if("column_value".equals(v.getDefaultValueType())){
                        Object defaultValue = itemList.get(0).get(v.getDefaultValue());
                        v.setDefaultValue(defaultValue == null ? "":defaultValue.toString());
                    }
                }

                tempId--;   //给新增字段配置的字段ID，为了不与数据库配置字段ID重复，使用负数做临时ID

                List<BusinessAttrCfgVO> newItemAttrCfg = new ArrayList<>();
                for(int i = 1;i<itemList.size();i++){
                    //先在每组项目字段前面添加一个项目名称字段配置
                    newItemAttrCfg.add(
                            BusinessAttrCfgVO.newInitInstance(tempId,"creditItem_itemName",
                                    "授信项目名称","text",oneAttrCfg.getColumnGroup(),
                                    oneAttrCfg.getClassifyName(),"fixed_value",
                                    itemList.get(i).get("creditItem_itemName").toString(),Long.parseLong(itemList.get(i).get("id").toString()))
                    );

                    //表达式内只能用常量
                    final int idx = i;
                    //每条授信项目信息都生成一组项目字段配置,并关联上项目ID
                    newTempList.forEach(itemAttr -> {
                        BusinessAttrCfgVO vo = new BusinessAttrCfgVO();
                        BeanUtils.copyProperties(itemAttr,vo);
                        vo.setProjectId(Long.parseLong(itemList.get(idx).get("id").toString()));
                        //读取默认值
                        if("column_value".equals(vo.getDefaultValueType())){
                            Object defaultValue = itemList.get(idx).get(vo.getDefaultValue());
                            vo.setDefaultValue(defaultValue == null ? "":defaultValue.toString());
                        }
                        newItemAttrCfg.add(vo);
                    });

                    tempId--; //每次产生新的临时ID
                }

                //获取授信项目最后一个字段的当前下标（上面代码有新插入过一条记录，所以要在最后获取）
                int lastIndex = workFlowCfg.getBusinessAttrCfgVOList().indexOf(itemAttrList.get(itemAttrList.size()-1));
                //将新增的项目字段配置插入最后一个项目字段配置的后面
                workFlowCfg.getBusinessAttrCfgVOList().addAll(lastIndex+1,newItemAttrCfg);

            }
        });
    }

    /**
     * 剔除掉页面提交不需要保存的字段
     * @param formDataList
     * @param attrCfgList
     */
    public static void rejectUselessAttr(List<AuditFormDTO> formDataList, List<BusinessAttrCfgVO> attrCfgList){
        if(formDataList == null || formDataList.isEmpty()){
            return;
        }

        if(attrCfgList == null || attrCfgList.isEmpty()){
            return;
        }

        List<AuditFormDTO> rmData = new ArrayList<>();
        formDataList.forEach(formData -> {
            String columnName = formData.getColumnName();
            if(columnName.indexOf("_") == -1 && columnName.indexOf(" ") == -1){
                rmData.add(formData);
                return;
            }
            //兼容参数提交时，分隔符是空格的
            if(columnName.indexOf("_") == -1){
                columnName = columnName.replace(" ","_");
            }

            final String tempColumnName = columnName;
            List<BusinessAttrCfgVO> temp =
                    attrCfgList.stream().filter(attrCfg -> attrCfg.getColumnName().equals(tempColumnName)).collect(Collectors.toList());
            if(temp == null || temp.isEmpty()){
                rmData.add(formData);
            }
        });

        //删除掉无用字段
        formDataList.removeAll(rmData);

    }

    /**
     * 根据扭转权限（流程扭转配置用户组与用户ID）过滤流程扭转配置
     * @param trailingWorkFlowCfg
     * @param userId
     * @param roleList
     * @return
     */
    public static List<BusinessWorkFlowExtendVO> powerFilterWorkFlowCfg(List<BusinessWorkFlowExtendVO> trailingWorkFlowCfg, Long userId, List<Role> roleList){
        if(trailingWorkFlowCfg == null || trailingWorkFlowCfg.isEmpty()){
            return trailingWorkFlowCfg;
        }

        if(userId == null && (roleList == null || roleList.isEmpty())){
            return new ArrayList<BusinessWorkFlowExtendVO>();
        }

        //排除登录用户没有权限的流程扭转配置
        List<BusinessWorkFlowExtendVO> newtrailingWorkFlowCfg = trailingWorkFlowCfg.stream().filter(workFlowCfg -> {
            //如果该流程没有配置权限，则所有人都有权处理
            if(StringUtil.isEmpty(workFlowCfg.getUserIds()) && StringUtil.isEmpty(workFlowCfg.getRoleIds())){
                return true;
            }
            if(!StringUtil.isEmpty(workFlowCfg.getUserIds())){
                List<String> userIds = Arrays.asList(workFlowCfg.getUserIds().split(","));
                if(userIds.contains(userId.toString())){
                    return true;
                }
            }
            if(!StringUtil.isEmpty(workFlowCfg.getRoleIds())){
                List<String> roleIds = Arrays.asList(workFlowCfg.getRoleIds().split(","));
                List<String> userRoleIds = roleList.stream().map(role -> role.getId().toString()).collect(Collectors.toList());
                for(String userRoleId:userRoleIds){
                    if(roleIds.contains(userRoleId)){
                        return true;
                    }
                }
            }

            return false;
        }).collect(Collectors.toList());

        if(newtrailingWorkFlowCfg == null || newtrailingWorkFlowCfg.isEmpty()){
            return new ArrayList<BusinessWorkFlowExtendVO>();
        }

        return newtrailingWorkFlowCfg;
    }

    //获取下一步处理人和角色
    public static Map getNextDispose(Long businessTypeId,String flowCode){
        //获取业务的所有流转
        List<BusinessWorkFlowExtendVO> businessWorkFlowList = getBusinessWorkFlowExtendList(businessTypeId);
        if (businessWorkFlowList == null) return null;

        //过滤出当前状态等于入参状态的流转
        businessWorkFlowList = businessWorkFlowList.stream()
                .filter(businessWorkFlow -> businessWorkFlow.getBeforeFlow().equals(flowCode))
                .collect(Collectors.toList());
        if (businessWorkFlowList == null) return null;
        //拼接
        String disposeUserIds = businessWorkFlowList.stream().map(businessWorkFlow -> businessWorkFlow.getUserIds()).collect(Collectors.joining(","));
        String disposeRoleIds = businessWorkFlowList.stream().map(businessWorkFlow -> businessWorkFlow.getRoleIds()).collect(Collectors.joining(","));

        //去重
        disposeUserIds = StringUtil.stringDistinct(disposeUserIds,",");
        disposeRoleIds = StringUtil.stringDistinct(disposeRoleIds,",");

        Map retMap = new HashMap();
        retMap.put("disposeUserIds",disposeUserIds);
        retMap.put("disposeRoleIds",disposeRoleIds);
        return retMap;
    }


    /**
     * 获取指定公司、业务的字段值配置（易用结构）
     * @param companyId
     * @param typeId
     * @return
     */
    public static Map<String, Object> getBusinessAttrValMap(Long companyId, Long typeId){
        if(companyId == null || typeId == null){
            return null;
        }
        //取缓存
        Map<String,Map>  mapByTypeId = (Map<String, Map>) redisUtil.hget(RedisConstant.REDIS_KEY_BUSINESS_ATTR_VAL,companyId.toString());

        Map<String,List<BusinessAttrVal>> mapByAttrId = mapByTypeId.get(typeId.toString());

        if(mapByAttrId == null){
            return null;
        }

        //将字段值集合封装成易用结构
        Map<String, Object> businessAttrValMap = new HashMap<>();
        mapByAttrId.keySet().forEach(key -> {
            List<BusinessAttrVal> attrValList = mapByAttrId.get(key);
            Map<String, BusinessAttrVal> attrValMap = attrValList.stream().collect(Collectors.toMap(attrVal -> attrVal.getId().toString(), attrVal -> attrVal));
            Map<String, Object> attrValObj = new HashMap<>();
            attrValObj.put("attrValMap", attrValMap);
            attrValObj.put("attrValList", attrValList);
            businessAttrValMap.put(key, attrValObj);
        });

        return businessAttrValMap;
    }

    /**
     * 根据字段属性获取字段封闭值集合对象（易用）
     * @param businessAttr
     * @param businessAttrValMap
     * @return
     */
    public static Map<String, BusinessAttrVal> getAttrValMap(BusinessAttrCfgVO businessAttr,Map<String, Object> businessAttrValMap){
        if(businessAttr == null || businessAttr.getBusinessAttrId() == null || businessAttrValMap == null){
            return null;
        }

        if(!businessAttrValMap.containsKey(businessAttr.getBusinessAttrId().toString())){
            return null;
        }
        Map<String, Object> attrValObj = (Map<String, Object>)businessAttrValMap.get(businessAttr.getBusinessAttrId().toString());
        Map<String, BusinessAttrVal> attrValMap = (Map<String, BusinessAttrVal>)attrValObj.get("attrValMap");
        return attrValMap;
    }

    /**
     * 根据字段属性与字段属性值ID获取字段封闭值对象（易用）
     * @param businessAttr
     * @param businessAttrValMap
     * @param attrValId
     * @return
     */
    public static BusinessAttrVal getAttrVal(BusinessAttrCfgVO businessAttr,Map<String, Object> businessAttrValMap,String attrValId){
        Map<String, BusinessAttrVal> attrValMap = BusinessCfgUtil.getAttrValMap(businessAttr,businessAttrValMap);
        if(attrValMap == null){
            return null;
        }

        if(StringUtil.isEmpty(attrValId)){
            return null;
        }

        return attrValMap.get(attrValId);
    }

}
