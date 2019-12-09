package com.zhjs.scfcloud.util.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举池
 * @author: dailongting
 * @date:2019/5/25 17:52
 */
public class EnumPool {

    public static final Map<BaseEnum, EnumBean> enumPool = new ConcurrentHashMap<BaseEnum, EnumBean>();

    public static void put(BaseEnum baseEnum,Integer status,String statusCH){
        enumPool.put(baseEnum, new EnumBean(status,statusCH));
    }

    public static EnumBean get(BaseEnum baseEnum){
        return enumPool.get(baseEnum);
    }
}
