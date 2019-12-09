package com.zhjs.scfcloud.ticket;

import com.jd.jr.cbp.sdk.entity.CommonResponse;
import com.jd.jr.cbp.sdk.entity.basic.response.QueryCompanyInfoRespBody;
import com.jd.jr.cbp.sdk.entity.basic.response.QueryUserCenterUrlRespBody;
import com.jd.jr.cbp.sdk.entity.billpay.common.AccountDetail;
import com.jd.jr.cbp.sdk.entity.billpay.common.BillDetail;
import com.jd.jr.cbp.sdk.entity.billpay.request.ApplyAgencyDefpayReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.request.CreateOrderReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.request.QueryEcdsAuthReqDto;
import com.jd.jr.cbp.sdk.entity.billpay.request.VerifyAgencyDefpayReqBody;
import com.jd.jr.cbp.sdk.entity.billpay.response.*;
import com.jd.jr.cbp.sdk.entity.ocr.request.OcrReqBody;
import com.jd.jr.cbp.sdk.entity.ocr.response.OcrRespBody;
import com.zhjs.scfcloud.util.util.JDZPUtil;
import com.zhjs.scfcloud.util.util.JsonUtil;
import com.zhjs.scfcloud.util.util.SmsUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 京东智票接口测试
 * @author: dailongting
 * @date:2019/7/13 15:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdzpApiTests {

    @Autowired
    JDZPUtil jdzpUtil;
    @Autowired
    SmsUtil smsUtil;

//    @Test
    public void queryProvincesAndCitys(){
        CommonResponse<QueryProvincesAndCitysRespBody> resp = jdzpUtil.queryProvincesAndCitys("440");
        System.out.println(resp.getData().toString());
    }

    @Test
    public void ocrRecognizeTest(){
        try {
            InputStream in = new FileInputStream("D:\\img\\电子商票-汉鑫.PNG");
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            BASE64Encoder encoder = new BASE64Encoder();
            String imgBASE64 = encoder.encode(data);

            CommonResponse<OcrRespBody> resp = jdzpUtil.ocrRecognize(imgBASE64, OcrReqBody.IMG_TYPE_FRONT);
            System.out.println(resp.getData().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryUserBindAccountsTest(){
        CommonResponse<QueryUserBindAccountsRespBody> resp = jdzpUtil.queryUserBindAccounts("43",1);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryBankList(){
        CommonResponse<QueryBankListRespBody> resp = jdzpUtil.queryBankList();
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryBankCnapsList(){
        CommonResponse<QueryBankCnapsListRespBody> resp = jdzpUtil.queryBankCnapsList(302,"5840");
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void applyAgencyDefpay(){
        ApplyAgencyDefpayReqBody body = new ApplyAgencyDefpayReqBody();
        body.setPlatformUserId("43")
                .setBankCode("CITIC")
                .setBankName("中信银行")
        ;
        body.setBankCnaps("302584043105");
        body.setBankChildName("中信银行深圳分行");
        body.setBankAccountName("陈伟杰总公司");
        body.setBankAccountNo("8110701013101237129");
        body.setProvinceId("440");
        body.setProvinceName("广东省");
        body.setCityId("5840");
        body.setCityName("深圳市");
        CommonResponse<ApplyAgencyDefpayRespBody> resp = jdzpUtil.applyAgencyDefpay(body);
        System.out.println(resp.getData().getResponseCode());
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void verifyAgencyDefpay(){
        VerifyAgencyDefpayReqBody reqBody = new VerifyAgencyDefpayReqBody();
        reqBody.setPlatformUserId("weldge@icloud.com");
        reqBody.setPayOrderId(1775L);
        reqBody.setAmount(1L);
        CommonResponse<VerifyAgencyDefpayRespBody> resp = jdzpUtil.verifyAgencyDefpay(reqBody);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void disableAccount(){
        CommonResponse<UnBindAccountRespBody> resp = jdzpUtil.disableAccount("weldge@icloud.com","2019071513523702299880549");
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryEcdsAccountAuth(){
        QueryEcdsAuthReqDto req = new QueryEcdsAuthReqDto();
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setBankCode("PAB");
        accountDetail.setAccountNo("8110701013254321237");
        req.setBuyerAccount(accountDetail);
        CommonResponse<QueryEcdsAuthRespDto> resp = jdzpUtil.queryEcdsAccountAuth(req);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryCompanyInfo(){
        CommonResponse<QueryCompanyInfoRespBody> resp = jdzpUtil.queryCompanyInfo("小米科技有限责任公司", null);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryUserCenterUrl(){
        CommonResponse<QueryUserCenterUrlRespBody> resp = jdzpUtil.queryUserCenterUrl("领筑数字科技有限公司","weldge@icloud.com");
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void getCashierUrl(){
        CommonResponse<GetCashierUrlRespBody> resp = jdzpUtil.getCashierUrl("585542558455554");
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void handleOrder(){
        CommonResponse<HandleOrderRespBody> resp = jdzpUtil.handleOrder("9898989898",2);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void createOrder(){
        //创建请求参数对象
        CreateOrderReqBody createOrderReqBody = new CreateOrderReqBody();
        //没有特殊注明可为空的参数就是必须传值
        //平台请求流水号
        createOrderReqBody.setPlatformReqNo("9898989898");
        //平台订单号
        createOrderReqBody.setPlatformOrderNo("25252525");
        //平台下单时间: yyyyMMddHHmmss
        createOrderReqBody.setPlatformOrderDate("20190718170500");
        //平台合同编码
        createOrderReqBody.setPlatformContractNo("20190719104645");
        //订单过期时间 yyyyMMddHHmmss
        createOrderReqBody.setExpireDate("20190722172600");
        //贴现率 可为空(不写此值)
        createOrderReqBody.setTransRate("3.4");
        //交易金额（不含手续费），单位：分
        createOrderReqBody.setTransAmt(10000L);
        //手续费金额，单位：分
        createOrderReqBody.setTransCharge(1L);
        //买方用户平台方ID
        createOrderReqBody.setBuyerUserId("weldge@icloud.com");

        //创建账户详情对象
        AccountDetail accountDetail = new AccountDetail();
        //绑定ID，通过2#接口可查询到
        accountDetail.setBindId("2019071517365422192181002");
        //开户行联行号
        accountDetail.setBankCnaps("302584043105");
        //银行账号
        accountDetail.setAccountNo("8110701013201237125");
        //买方收票账户（ECDS验证）
        createOrderReqBody.setBuyerAccount(accountDetail);

        //创建账户详情对象
        AccountDetail accountDetai2 = new AccountDetail();
        //绑定ID，通过2#接口可查询到
        accountDetai2.setBindId("2019071515302216739687729");
        //开户行联行号
        accountDetai2.setBankCnaps("104584000003");
        //银行账号
        accountDetai2.setAccountNo("22222222222222");
        //卖方用户平台方ID
        createOrderReqBody.setSellerUserId("43");
        //卖方收款卖方持票账户账户（放款）
        createOrderReqBody.setSellerAccountA(accountDetai2);
        //（ECDS验证）
        createOrderReqBody.setSellerAccountB(accountDetai2);

        //创建票据对象
        BillDetail billDetail = new BillDetail();
        //票据号码
        billDetail.setBillNo("230958400510220190529405927624");
        //票面金额，单位：分
        billDetail.setBillAmt(30780050L);
        //出票人名称 可为空(可不传此值)
        billDetail.setDrawerName("深华建设(深圳)股份有限公司");
        //出票人开户行行号 可为空(可不传此值)
//        billDetail.setDrawerBank("123456");
        //出票人开户账号 可为空(可不传此值)
//        billDetail.setDrawerAccount("234142353432");
        //承兑人名称  可为空(可不传此值)
        billDetail.setAccepterName("深华建设(深圳)股份有限公司");
        //承兑人开户行行号 可为空(可不传此值)
        billDetail.setAccepterBank("309584005102");
        //承兑人开户账号 可为空(可不传此值)
        billDetail.setAccepterAccount("462354");
        //收款人名称 可为空(可不传此值)
        billDetail.setPayeeName("汉鑫钢铁(珠海)有限公司");
        //收款人开户行行号 可为空(可不传此值)
        billDetail.setPayeeBank("");
        //收款人开户账号 可为空(可不传此值)
        billDetail.setPayeeAccount("");
        //票据信息
        createOrderReqBody.setBillDetail(billDetail);

        CommonResponse<CreateOrderRespBody> resp = jdzpUtil.createOrder(createOrderReqBody);
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void queryOrder(){
        CommonResponse<QueryOrderRespBody> resp = jdzpUtil.queryOrder("9898989898");
        System.out.println(JsonUtil.toJSON(resp.getData()));
    }

    @Test
    public void commTests(){
        String urlEncoder = null;
        try {
            urlEncoder = URLEncoder.encode("http://evb4jy.natappfree.cc/platform/jdzpNotify","utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(urlEncoder);
    }


    @Test
    public void getSmsTpl(){
        String result = smsUtil.lzySmstpl();
        System.out.println(result);
    }

    @Test
    public void lzySmsBsend(){
        Map<String, String> params = new HashMap<>();
        params.put("code", "687544");

        String result = smsUtil.lzySmsBsend("13088854921", "S507", params);
        System.out.println(result);
    }

}
