package com.zhjs.scfcloud.util.util;

import java.math.BigDecimal;

/**
 * @author: dailongting
 * @date:2019/11/12 17:09
 */
public class BigDecimalUtil {

    public static BigDecimal orElse(BigDecimal o){
        if ( o == null) {
            return BigDecimal.ZERO;
        }

        return o;
    }
}
