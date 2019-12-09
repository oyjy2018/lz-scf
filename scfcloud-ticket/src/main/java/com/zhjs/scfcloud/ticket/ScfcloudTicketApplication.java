package com.zhjs.scfcloud.ticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.zhjs.scfcloud.model.mapper"})
@ComponentScan(basePackages = {"com.zhjs.scfcloud.util","com.zhjs.scfcloud.model","com.zhjs.scfcloud.ticket"})
public class ScfcloudTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudTicketApplication.class, args);
    }

}
