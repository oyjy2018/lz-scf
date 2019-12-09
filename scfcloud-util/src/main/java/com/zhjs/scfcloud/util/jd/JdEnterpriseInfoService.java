package com.zhjs.scfcloud.util.jd;

import com.jd.jr.jropen.sdk.api.BaseService;
import com.jd.jr.jropen.sdk.api.EnterRealNameService;
import com.jd.jr.jropen.sdk.api.EnterpriseInfoService;
import com.jd.jr.jropen.sdk.api.HapiLoginService;
import com.jd.jr.jropen.sdk.model.CommonRequest;
import com.jd.jr.jropen.sdk.model.CommonResponse;
import com.jd.jr.jropen.sdk.model.enter.*;
import org.springframework.util.StringUtils;

/**
 * 京东开发平台-接口服务类
 */
public class JdEnterpriseInfoService {

    private static EnterpriseInfoService enterpriseInfoService = new EnterpriseInfoService();

    private static EnterRealNameService enterRealNameService= new EnterRealNameService();

    private static HapiLoginService hapiLoginService = new HapiLoginService();
    /**
     * 企业账户注册接口
     * @return
     */
    public static CommonResponse<EnterRegisterResponseBody> register(String merCustomerId,String jrMerMobile){
        CommonRequest<EnterRegisterRequestBody> req = new CommonRequest<EnterRegisterRequestBody>();
        req.setRequestHeader(CommonRequestHeaderUtil.initCommonRequestHeader());

        EnterRegisterRequestBody reqBody = new EnterRegisterRequestBody();
        reqBody.setAuthorize("1");// 0:用户未授权，1：用户已授权；
        reqBody.setJrMerMobile(jrMerMobile);
        reqBody.setMerCustomerId(merCustomerId);
        req.setRequestBody(reqBody);

        return enterpriseInfoService.register(req);
    }

    /**
     * 账户信息查询接口
     * @return
     */
    public static CommonResponse<EnterQueryInfoResponseBody> queryInfo(String merCustomerId){
        CommonRequest<EnterQueryInfoRequestBody> req = new CommonRequest<>();
        req.setRequestHeader(CommonRequestHeaderUtil.initCommonRequestHeader());

        EnterQueryInfoRequestBody reqBody = new EnterQueryInfoRequestBody();
        reqBody.setMerCustomerId(merCustomerId);
        req.setRequestBody(reqBody);

        return enterpriseInfoService.query(req);
    }

    /**
     * 实名信息查询接口
     * @return
     */
    public static CommonResponse<EnterRealNameRespBody> queryEnterRealNameInfo(String merCustomerId,String partnerApplyId){
        CommonRequest<EnterRealNameReqBody> req = new CommonRequest<>();
        req.setRequestHeader(CommonRequestHeaderUtil.initCommonRequestHeader());

        EnterRealNameReqBody reqBody = new EnterRealNameReqBody();
        reqBody.setMerCustomerId(merCustomerId);
        if(!StringUtils.isEmpty(partnerApplyId)){
            reqBody.setPartnerApplyId(partnerApplyId);
        }
        req.setRequestBody(reqBody);

        return enterRealNameService.queryEnterRealNameInfo(req);
    }

    /**
     * H5组件通用调用方案接口(返回请求链接)
     * @return
     */
    public static String initJdLoginUrl(String merCustomerId){
        CommonRequest<HapiLoginRequestBody> req = new CommonRequest<>();
        req.setRequestHeader(CommonRequestHeaderUtil.initCommonRequestHeader());

        HapiLoginRequestBody requestBody = new HapiLoginRequestBody();
        requestBody.setMerCustomerId(merCustomerId);
        requestBody.setComponentCode("EPP_REAL_NAME_1.0PC");
        requestBody.setLoginRole("ENTER");

        req.setRequestBody(requestBody);

        return new BaseService().getRedirectUrl(req);
    }


}
