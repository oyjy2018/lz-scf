package com.zhjs.scfcloud.util.config;

import com.jd.jr.cbp.sdk.client.DefaultCbpClient;
import com.jd.jr.cbp.sdk.services.basic.BasicInfoService;
import com.jd.jr.cbp.sdk.services.billpay.BankCardService;
import com.jd.jr.cbp.sdk.services.billpay.EcdsOrderService;
import com.jd.jr.cbp.sdk.services.billpay.OrderService;
import com.jd.jr.cbp.sdk.services.notify.NotifyService;
import com.jd.jr.cbp.sdk.services.ocr.RecognizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author: dailongting
 * @date:2019/7/11 11:08
 */
@Configuration
public class JDZPConfig {

    @Autowired
    private Environment env;

    /**
     * 封装京东智票SDK通用配置（平台ID,秘钥，加密因子，服务调用地址等）
     * @return
     */
    @Bean
    public com.jd.jr.cbp.sdk.common.config.Configuration getConfiguration(){
        com.jd.jr.cbp.sdk.common.config.Configuration config = new com.jd.jr.cbp.sdk.common.config.Configuration();
        config.setPlatformId(env.getProperty("cbp.platformId"));
        config.setServer(env.getProperty("cbp.server"));
        config.setPrivateCert(env.getProperty("cbp.privateCert"));
        config.setPublicCert(env.getProperty("cbp.publicCert"));
        config.setPrivatePwd(env.getProperty("cbp.privatePwd"));
        return config;
    }

    /**
     * 京东智票核心类（调用请求，加密参数）
     * @return
     */
    @Bean
    public DefaultCbpClient getDefaultCbpClient(){
        DefaultCbpClient client = new DefaultCbpClient();
        client.setConfiguration(getConfiguration());
        return client;
    }

    /**
     * 回调接口参数处理类
     * @return
     */
    @Bean
    public NotifyService getNotifyService(){
        NotifyService notifyService = new NotifyService();
        notifyService.setClient(getDefaultCbpClient());
        return notifyService;
    }

    @Bean
    public BankCardService getBankCardService(){
        BankCardService bankCardService = new BankCardService();
        bankCardService.setClient(getDefaultCbpClient());
        return bankCardService;
    }

    @Bean
    public OrderService getOrderService(){
        OrderService orderService = new OrderService();
        orderService.setClient(getDefaultCbpClient());
        return orderService;
    }

    /**
     * OCR识别类
     * @return
     */
    @Bean
    public RecognizeService getRecognizeService(){
        RecognizeService recognizeService = new RecognizeService();
        recognizeService.setClient(getDefaultCbpClient());
        return recognizeService;
    }

    @Bean
    public EcdsOrderService getEcdsOrderService(){
        EcdsOrderService ecdsOrderService = new EcdsOrderService();
        ecdsOrderService.setClient(getDefaultCbpClient());
        return ecdsOrderService;
    }

    @Bean
    public BasicInfoService getBasicInfoService(){
        BasicInfoService basicInfoService = new BasicInfoService();
        basicInfoService.setClient(getDefaultCbpClient());
        return basicInfoService;
    }
}
