package com.zhjs.scfcloud.model.util;

import com.zhjs.scfcloud.model.vo.business.BusinessWorkFlowFileVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dailongting
 * @date:2019/6/21 15:56
 */
public class RedisInitCfgUtil {

    private static Map<String, Object> getMap(Map<String, Object> map, String key){
        Object obj = map.get(key);
        if(obj == null){
            obj = new HashMap<String, Object>();
        }
        return ( Map<String, Object>)obj;
    }

    /**
     * 根据公司ID和业务类型ID将流程扭转附件配置信息封装成map
     * @param workFlowFileVOList
     * @return
     */
    public static Map<String, Object> createBusinessWorkFlowFileCfgMap(List<BusinessWorkFlowFileVO> workFlowFileVOList){
        if(workFlowFileVOList == null || workFlowFileVOList.isEmpty()) return null;

        Map<String, Object> cfgMap = new HashMap<>();
        workFlowFileVOList.forEach(vo -> {
            Map<String, Object> companyMap = getMap(cfgMap, vo.getCompanyId().toString());

            Object obj = companyMap.get(vo.getBusinessTypeId().toString());
            if(obj == null){
                obj = new ArrayList<BusinessWorkFlowFileVO>();
            }
            List<BusinessWorkFlowFileVO> fileCfgList = (List<BusinessWorkFlowFileVO>) obj;
            fileCfgList.add(vo);
            companyMap.put(vo.getBusinessTypeId().toString(),fileCfgList);
            cfgMap.put(vo.getCompanyId().toString(),companyMap);
        });

        return cfgMap;
    }
}
