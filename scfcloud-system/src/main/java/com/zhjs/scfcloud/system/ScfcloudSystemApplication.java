package com.zhjs.scfcloud.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan(basePackages = {"com.zhjs.scfcloud.model.mapper"})
@ComponentScan(basePackages = {"com.zhjs.scfcloud.util","com.zhjs.scfcloud.model","com.zhjs.scfcloud.system"})
public class ScfcloudSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudSystemApplication.class, args);
    }

}
