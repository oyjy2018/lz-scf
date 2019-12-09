package com.zhjs.scfcloud.util.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    private static Environment env;

    @Autowired
    public void setEnv(Environment env){
        PropertiesUtil.env = env;
    }

    public static String getProperty(String key){
        return env.getProperty(key);
    }

    public static Integer getPropertyToInt(String key){
        String val = env.getProperty(key);
        if(!StringUtils.isNumeric(val)) return null;
        return Integer.valueOf(val);
    }
}
