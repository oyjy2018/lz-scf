package com.zhjs.scfcloud.model.util;

import com.zhjs.scfcloud.model.entity.Dictionary;
import com.zhjs.scfcloud.model.entity.DictionaryDetails;
import com.zhjs.scfcloud.util.constant.RedisConstant;
import com.zhjs.scfcloud.util.util.RedisUtil;
import com.zhjs.scfcloud.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据字典工具类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-14 16:07
 *
 * @author liuchanghai
 * @create 2019-06-14 16:07
 * @since
 */

@Component
public class DictionaryUtil {

    private static Logger logger = LoggerFactory.getLogger(BusinessCfgUtil.class);

    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil){
        DictionaryUtil.redisUtil = redisUtil;
    }

    public static RedisUtil getRedisUtil(){
        return DictionaryUtil.redisUtil;
    }


    /**
     * 读取 redis 获取数据字典父类表信息
     * 筛选层级 dictKey > status
     * dictKey 不能为空
     * status 为空时不做状态筛选
     */
    public List<Dictionary> getDictionaryList(String dictKey, Integer status){
        //返回结果集
        List<Dictionary> dictionaryList = new ArrayList<>();
        dictionaryList = (List<Dictionary>) redisUtil.hget(RedisConstant.REDIS_KEY_DICTIONARY,dictKey);
        if (dictionaryList == null){
            return dictionaryList;
        }
        if (status != null){
            dictionaryList = dictionaryList.stream()
                    .filter(dictionaryDetails -> dictionaryDetails.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        return dictionaryList;
    }

    /**
     * 根据父编码获取字典表数据
     * @param parentKey
     * @return
     */
    public static List<DictionaryDetails> getDictionaryDetailsByParentKey(String parentKey) {
        Optional<List<DictionaryDetails>> optional = Optional.ofNullable((List<DictionaryDetails>) redisUtil.hget(RedisConstant.REDIS_KEY_DICTIONARY_DETAILS,parentKey));
        return optional.orElse(new ArrayList<DictionaryDetails>());
    }


    /**
     * 读取 redis 获取数据字典表信息
     * 筛选层级 parentKey > remark1 > dictKey
     * parentKey 不能为空
     * remark1 为空时取 parentKey 的全部
     * dictKey 为空时取 remark1 的全部
     * status 为空时不做状态筛选
     */
    public List<DictionaryDetails> getDictionaryDetailsList(String parentKey, String remark1,String dictKey, Integer status){
        //返回结果集
        List<DictionaryDetails> dictionaryDetailsList = new ArrayList<>();
        dictionaryDetailsList = (List<DictionaryDetails>) redisUtil.hget(RedisConstant.REDIS_KEY_DICTIONARY_DETAILS,parentKey);
        if (dictionaryDetailsList == null){
           return dictionaryDetailsList;
        }
        if (!StringUtil.isEmpty(remark1)){
            dictionaryDetailsList = dictionaryDetailsList.stream()
                    .filter(dictionaryDetails -> dictionaryDetails.getRemark1().equals(remark1))
                    .collect(Collectors.toList());
        }
        if (!StringUtil.isEmpty(dictKey)){
            dictionaryDetailsList = dictionaryDetailsList.stream()
                    .filter(dictionaryDetails -> dictionaryDetails.getDictKey().equals(dictKey))
                    .collect(Collectors.toList());
        }
        if (status != null){
            dictionaryDetailsList = dictionaryDetailsList.stream()
                    .filter(dictionaryDetails -> dictionaryDetails.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        return dictionaryDetailsList;
    }

}
