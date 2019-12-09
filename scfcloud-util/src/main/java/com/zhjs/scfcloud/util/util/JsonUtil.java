package com.zhjs.scfcloud.util.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.catalina.User;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class JsonUtil {

    public static <T> T jsonToBean(String json, Class<T> c){
        return JSON.parseObject(json, c);
    }

    public static <T> T mapToBean(Map map, Class<T> c){
        return JSON.parseObject(toJSON(map), c);
    }

    public static Map beanToMap(Object obj){
        return jsonToMap(toJSON(obj));
    }

    public static <T> List<T> jsonToBeanList(String json , Class<T> c){
        return JSON.parseArray(json, c);
    }

    public static Map jsonToMap(String json){
        return JSON.parseObject(json, HashMap.class);
    }

    public static List<HashMap> jsonToMapList(String json){
        return JSON.parseArray(json, HashMap.class);
    }

    public static String toJSON(Object o){
        return JSON.toJSONString(o);
    }

    public static String toSerializerJSON(Object o){
        return JSON.toJSONString(o, SerializerFeature.WriteMapNullValue);
    }

}
