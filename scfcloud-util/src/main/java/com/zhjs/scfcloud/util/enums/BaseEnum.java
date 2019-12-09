package com.zhjs.scfcloud.util.enums;

/**
 * @author: dailongting
 * @date:2019/5/25 17:57
 */
public interface BaseEnum {

    default EnumBean get(){
        return EnumPool.get(this);
    }
}
