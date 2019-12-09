package com.zhjs.scfcloud.util.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 手机验证码发送工具类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-01 16:09
 *
 * @author liuchanghai
 * @create 2019-06-01 16:09
 * @since
 */
@Component
public class SmsUtil {

    @Value("${lzCloudSmsUrl}")
    private String smsUrl;

    @Value("${lingzhuyun.smsUrl}")
    private String lzySmsUrl;

    @Value("${lingzhuyun.bizChannel}")
    private String lzyBizChannel;

    @Value("${lingzhuyun.bizSecret}")
    private String lzyBizSecret;

    @Value("${smsSendSwitch}")
    private Integer smsSendSwitch;

    private static final String HTTP_URL_MSG_SMS_TPL = "/msg/sms/tpl";
    private static final String HTTP_URL_MSG_SMS_BSEND = "/msg/sms/bsend";

    /**
     * 短信模板编码
     * @return
     */
    public static final String lzy_sms_type_S501 = "S501";  //领筑系统创建订单
    public static final String lzy_sms_type_S502 = "S502";  //买家已签署合同，订单状态变更为【待卖家签合同】
    public static final String lzy_sms_type_S503 = "S503";  //卖方已签合同，订单状态变更为【支付成功-待背书】
    public static final String lzy_sms_type_S504 = "S504";  //支付成功-待背书
    public static final String lzy_sms_type_S505 = "S505";  //已背书-待签收
    public static final String lzy_sms_type_S506 = "S506";  //小额打款成功
    public static final String lzy_sms_type_S507 = "S507";  //用户注册
    public static final String lzy_sms_type_S508 = "S508";  //用户修改手机号
    public static final String lzy_sms_type_S509 = "S509";  //修改密码
    public static final String lzy_sms_type_S510 = "S510";  //审批通过


    public String lzySmstpl(){
        Map<String,Object> params = new HashMap<>();
        params.put("bizChannel", lzyBizChannel);
        params.put("bizSecret", lzyBizSecret);
        String result = HttpUtil.doPostJson(lzySmsUrl+SmsUtil.HTTP_URL_MSG_SMS_TPL,JsonUtil.toJSON(params),"utf-8");
        return result;
    }

    public String lzySmsBsend(String mobile, String smsType, Map<String, String> params){
        //发送短信开关控制
        if(0 == smsSendSwitch) return "短信发送已关闭";

        Map<String,Object> reqParam = new HashMap<>();
        reqParam.put("bizChannel", lzyBizChannel);
        reqParam.put("bizSecret", lzyBizSecret);
        reqParam.put("smsType", smsType);
        reqParam.put("recieveMobile", mobile);
        reqParam.put("params", params);
        String result = HttpUtil.doPostJson(lzySmsUrl+SmsUtil.HTTP_URL_MSG_SMS_BSEND,JsonUtil.toJSON(reqParam),"utf-8");
        return result;
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param code
     * @return
     */
    public boolean sendSms(String phone, String code) throws IOException {

        /**
         * 请求地址
         */
//        String smsUrl = "http://192.168.1.252:7003/lz/sms/bsend";
        SmsEntity smsEntity = new SmsEntity();
        smsEntity.setBizChannel("SLZYC");
        smsEntity.setSmsType("S105");
        smsEntity.setRecieveMobile(phone);
        /**
         * 短信模版中需要的参数
         */
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        smsEntity.setParams(params);
        String send = sendPostDataByJson(smsUrl, JSON.toJSONString(smsEntity), "utf-8");
        Map sendResult = JSON.parseObject(send, HashMap.class);
        if(sendResult.get("msg").equals("ok") && sendResult.get("status").equals("200")){
            return true;
        }
        return false;
    }
    /**
     * post请求传输json数据
     * @param url 请求地址
     * @param json 发送的json数据
     * @param encoding 编码
     * @return 返回值
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendPostDataByJson(String url, String json, String encoding) throws ClientProtocolException, IOException {
        String result = "";
        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        // 设置参数到请求对象中
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentEncoding("utf-8");
        httpPost.setEntity(stringEntity);
        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), encoding);
        }
        // 释放链接
        response.close();
        return result;
    }
}
