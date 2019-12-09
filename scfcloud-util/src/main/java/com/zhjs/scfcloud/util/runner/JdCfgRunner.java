package com.zhjs.scfcloud.util.runner;

import com.jd.jr.jropen.sdk.util.PropertiesUtil;
import com.jd.jr.jropen.sdk.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author: dailongting
 * @date:2019/8/6 16:56
 */
@Component
@Order(1)
public class JdCfgRunner implements ApplicationRunner {

    @Value("${local.config.active}")
    private String localConfigActive;

    /**
     * 由于京东开放平台给的jar包内写死从classpath获取配置文件，
     * 为了避免发布时频繁手动切换配置文件（jenkins自动部署），在项目启动时根据配置自动从指定目录将配置文件复制到classpath目录下
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("--------------开始动态指定京东开发平台配置文件-------------------");
        System.out.println(Thread.currentThread().getContextClassLoader().getResourceAsStream(localConfigActive+"/jropen_sdk.properties"));

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(localConfigActive+"/jropen_sdk.properties");
        if(is != null){
            System.out.println("指定前京东开放平台配置文件版本为："+PropertiesUtil.getValue("current.active"));
            Properties properties = new Properties();
            properties.load(is);

            Class<PropertiesUtil> c = PropertiesUtil.class;
            Field field = c.getDeclaredField("properties");
            field.setAccessible(true);
            field.set(null,properties);
            System.out.println("指定后京东开放平台配置文件版本为："+PropertiesUtil.getValue("current.active"));
        }
        System.out.println("--------------结束动态指定京东开发平台配置文件-------------------");
    }
}
