package com.zhjs.scfcloud.ticket;

import com.jd.jr.jropen.sdk.model.CommonResponse;
import com.jd.jr.jropen.sdk.model.enter.EnterRealNameRespBody;
import com.jd.jr.jropen.sdk.model.enter.EnterRegisterResponseBody;
import com.zhjs.scfcloud.ticket.service.CompanyVerifiedService;
import com.zhjs.scfcloud.util.jd.JdEnterpriseInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 京东开放平台-单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdOpenApiTests {
    @Autowired
    private CompanyVerifiedService companyVerifiedService;
    @Test
    public void testRegister() {
        System.out.println("开始");
        System.out.println(Thread.currentThread().getContextClassLoader().getResourceAsStream("jropen_sdk.properties"));
        //企业注册接口
        CommonResponse<EnterRegisterResponseBody> commonResponse = JdEnterpriseInfoService.register("1","13246689983");
        System.out.println("=======================================");
        EnterRegisterResponseBody body = commonResponse.getResponseBody();
        System.out.println("请求是否成功：" + body.getResponseCode());
        System.out.println("请求体内容："+ body);
        System.out.println("===================================================");
    }


//    @Test
//    public void testQuery() {
//        //账户信息查询接口
//        CommonResponse<EnterQueryInfoResponseBody> commonResponse = JdEnterpriseInfoService.queryInfo("dev43");
//        System.out.println("=======================================================");
//        EnterQueryInfoResponseBody body = commonResponse.getResponseBody();
//        System.out.println("请求是否成功：" + body.getResponseCode());
//        System.out.println("请求体内容："+ body);
//        System.out.println("========================================================");
//    }

    @Test
    public void testQueryEnterRealNameInfo() {
        //实名信息查询接口
        CommonResponse<EnterRealNameRespBody> commonResponse = JdEnterpriseInfoService.queryEnterRealNameInfo("43","partner_lingzhu1563005371260");
        System.out.println("=======================================================");
        EnterRealNameRespBody body = commonResponse.getResponseBody();
        System.out.println("请求是否成功：" + body.getResponseCode());
        System.out.println("请求体内容："+ body);
        System.out.println("========================================================");
    }

    @Test
    public void initJdLoginUrl(){
        //H5组件通用调用方案接口(返回请求链接)
        System.out.println("=======================================================");
        try {
            System.out.println("请求体内容：" +"http://ft.jdpay.com/hapi/sign/loginIn?"+ JdEnterpriseInfoService.initJdLoginUrl("1"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("出现错误啦。");
        }
        System.out.println("========================================================");

    }

//    @Test
//    public void saveInfoForJdReview(){
//        //企业/个体工商户实名认证申请单审核结果通知
//        companyVerifiedService.saveInfoForJdReview("50","商户号","申请单号。","200","131");
//    }


}
