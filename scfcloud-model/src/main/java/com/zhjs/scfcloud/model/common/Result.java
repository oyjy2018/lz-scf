package com.zhjs.scfcloud.model.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhjs.scfcloud.util.util.JsonUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Result<T> {
    //设置字段值为NULL，依旧显示到json串
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private String message;
    private int code;
    private T data;

    /**
     * >=10000为成功码
     * < 10000为失败码
     * 10000=成功,9999=失败，其他请自行拓展
     */
    public static final int RESULT_CODE_SUCCESS = 10000;
    public static final int RESULT_CODE_FAILURE = 9999;
    public static final int RESULT_CODE_EXCEPTION = 8888;
    public static final int RESULT_CODE_LOGIN_INVALID = 9000; //登录失效专用  别处禁止使用

    public static final String DEFAULT_PAGE_NAME = "records"; //默认分页列表参数名
    public static final String DEFAULT_LIST_NAME = "list"; //默认列表参数名
    public static final String DEFAULT_TOTAL_NAME = "total"; //默认总记录参数名

    public static final String DEFAULT_SUCCESS_MESSAGE = "成功"; //默认成功名称
    public static final String DEFAULT_FAIL_MESSAGE = "失败"; //默认失败名称

    public Result(){

    }

    public Result(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static Result success(){
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T>Result success(String messsage){
        return new Result(RESULT_CODE_SUCCESS, messsage);
    }

    public static <T>Result success(int code, String messsage){
        return new Result(code, messsage);
    }

    public static <T>Result success(T data){
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static  <T>Result successData(T data) {
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }


    public static <T>Result success(String message, T data){
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static <T>Result success(int code, String message, T data){
        return new Result(code, DEFAULT_SUCCESS_MESSAGE, data);
    }

    //成功返回列表分页内容
    public static <T>Result successPage(Page page,String pageName){
        Map retMap = new HashMap();
        retMap.put(DEFAULT_TOTAL_NAME,page.getTotal());
        retMap.put(pageName,page.getRecords());
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, retMap);
    }

    //成功返回列表分页内容
    public static <T>Result successList(List list){
        Map retMap = new HashMap();
        retMap.put(DEFAULT_LIST_NAME,list);
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, retMap);
    }

    //成功返回列表分页内容
    public static <T>Result successPage(Page page){
        return successPage(page,DEFAULT_PAGE_NAME);
    }

    public static <T>Result failure(){
        return new Result(RESULT_CODE_FAILURE, DEFAULT_FAIL_MESSAGE);
    }

    public static <T>Result failure(String message){
        return new Result(RESULT_CODE_FAILURE, message);
    }

    public static <T>Result failure(String message, T data){
        return new Result(RESULT_CODE_FAILURE, DEFAULT_FAIL_MESSAGE, data);
    }

    public static Result failure(int code, String message) {
        Result result = new Result<>(code, message);
        return result;
    }

    public static <T>Result failure(int code, String message, T data){
        return new Result(code, DEFAULT_FAIL_MESSAGE, data);
    }

    public void copy(Result<T> result){
        this.code = result.getCode();
        this.message = result.getMessage();
        this.data = result.getData();
    }

    public static Result exception(int code, String message) {
        Result result = new Result<>(code, message);
        return result;
    }
    public static Result exception(String message) {
        Result result = new Result<>(RESULT_CODE_EXCEPTION, message);
        return result;
    }
    public static <T>Result exception(String message,T data) {
        Result result = new Result<>(RESULT_CODE_EXCEPTION, message,data);
        return result;
    }
    public String toJSON(){
        return JsonUtil.toJSON(this);
    }
    public String toSerializerJSON(){
        return JsonUtil.toSerializerJSON(this);
    }
}
