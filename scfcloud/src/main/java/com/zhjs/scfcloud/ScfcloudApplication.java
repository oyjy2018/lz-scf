package com.zhjs.scfcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

//引入的scfcloud-model项目因为需要使用mybatis plus 注解，对其进行了依赖，本项目没有DataSource相关配置，所以需要spring boot 注册这个类
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.zhjs.scfcloud.*","com.zhjs.scfcloud.util"})
public class ScfcloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudApplication.class, args);
    }

}
