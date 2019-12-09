package com.zhjs.scfcloud.model.util;

import com.zhjs.scfcloud.model.entity.BusinessAttrVal;
import com.zhjs.scfcloud.model.vo.business.BusinessAttrCfgVO;
import com.zhjs.scfcloud.util.enums.CommonEnum;
import com.zhjs.scfcloud.util.util.StringUtil;

import java.util.Map;

/**
 * 业务处理工具类
 * @author: dailongting
 * @date:2019/8/29 16:06
 */
public class BusinessUtil {

    /**
     * 将字段值封装入相关字段对象属性中（查询审核过程函数专用）
     * @param vo
     * @param businessAttrValMap
     * @param auditData
     * @param applyColumn
     */
    public static void setApplyColumn(BusinessAttrCfgVO vo, Map<String, Object> businessAttrValMap, Map<String, Object> auditData, Map<String, Object> applyColumn){
        applyColumn.put("columnCh", vo.getColumnCh());
        //判断是否是封闭式值
        if(vo.getHasClosedType().intValue() == CommonEnum.WhetherEnum.YES.getStatus().intValue()){
            //某些字段虽然是封闭式取值，但是值列表是由前端提供，比如地址
            if(businessAttrValMap.containsKey(vo.getBusinessAttrId().toString())){
                if(auditData.get(vo.getColumnName()) != null){
                    BusinessAttrVal attrVal = BusinessCfgUtil.getAttrVal(vo,businessAttrValMap, StringUtil.objToString(auditData.get(vo.getColumnName())));
                    if(attrVal != null){
                        applyColumn.put("columnVal", attrVal.getValueCh());
                    }
                }
            }else{
                applyColumn.put("columnVal", auditData.get(vo.getColumnName()));
            }
        }else{
            applyColumn.put("columnVal", auditData.get(vo.getColumnName()));
        }
        applyColumn.put("sort", vo.getSort());
    }
}
