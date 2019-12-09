package com.zhjs.scfcloud.util.util;

import com.zhjs.scfcloud.util.enums.BaseEnum;

import java.util.Objects;

/**
 * @author: dailongting
 * @date:2019/5/27 11:55
 */
public class EnumUtil {

    public static BaseEnum getBaseEnumByStatus(BaseEnum[] values,Integer status){
        if(status == null || values == null || values.length == 0) return null;
        for(BaseEnum baseEnum:values){
            if(Objects.equals(baseEnum.get().status,status)) return baseEnum;
        }
        return null;
    }
}
