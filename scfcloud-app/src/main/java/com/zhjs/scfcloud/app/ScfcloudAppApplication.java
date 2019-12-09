package com.zhjs.scfcloud.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.zhjs.scfcloud.app","com.zhjs.scfcloud.model","com.zhjs.scfcloud.util"})
public class ScfcloudAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudAppApplication.class, args);
    }

}
