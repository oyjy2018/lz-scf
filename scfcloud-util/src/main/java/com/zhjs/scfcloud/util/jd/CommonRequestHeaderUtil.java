package com.zhjs.scfcloud.util.jd;

import com.jd.jr.jropen.sdk.model.CommonRequestHeader;
import com.jd.jr.jropen.sdk.util.PropertiesUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 京东开放平台-请求头工具类
 */
public  class CommonRequestHeaderUtil {

    public static CommonRequestHeader initCommonRequestHeader(){
        CommonRequestHeader reqHeader = new CommonRequestHeader();
        reqHeader.setPartnerId(PropertiesUtil.getValue("partner.id"));//平台ID，由京东开放平台提供
        reqHeader.setAppId(PropertiesUtil.getValue("app.id"));//商户应用编码，由京东开放平台提供
        reqHeader.setVersion("1.0.0");
        String reqTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        reqHeader.setMerRequestNo("REQ" + reqTime);
        reqHeader.setMerRequestTime(reqTime);
        reqHeader.setCharset("UTF-8");
        reqHeader.setSignType("MD5");// MD5,CERT
        reqHeader.setEncryptType("3DES");

        return reqHeader;
    }

}
