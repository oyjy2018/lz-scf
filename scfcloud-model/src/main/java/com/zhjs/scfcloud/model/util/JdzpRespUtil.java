package com.zhjs.scfcloud.model.util;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.zhjs.scfcloud.model.common.Result;

/**
 * 京东响应对象处理工具类
 * @author: dailongting
 * @date:2019/7/16 9:54
 */
public class JdzpRespUtil {

    /**
     * 分析京东返回的结果集
     * @param response
     * @return
     */
    public static Result analyzeResponseJDZP(CommonResponse response){
        if (response == null) return Result.failure("京东智票接口无响应");
        if (response.getHeader() == null) return Result.failure("京东智票接口无响应头");
        if (!"00000".equals(response.getHeader().getCode())) {
            switch (response.getHeader().getCode()) {
                case "02000":return Result.failure("京东智票接口服务未知异常");
                case "02001":return Result.failure("京东智票接口服务签名失败");
                case "02002":return Result.failure("京东智票接口服务加密失败");
                case "02003":return Result.failure("京东智票接口服务验签失败");
                case "02004":return Result.failure("京东智票接口服务解密失败");
                default:return Result.failure("京东智票接口未知错误");
            }
        }
        if (response.getData() == null) return Result.failure("京东智票接口无响应实体");
        if (!"000000".equals(response.getData().getResponseCode())) return Result.failure("京东智票接口错误："+response.getData().getResponseDesc());
        return Result.success();
    }
}
