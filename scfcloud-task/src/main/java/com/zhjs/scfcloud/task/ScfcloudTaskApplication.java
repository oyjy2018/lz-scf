package com.zhjs.scfcloud.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@MapperScan(basePackages = {"com.zhjs.scfcloud.model.mapper"})
@ComponentScan(basePackages = {"com.zhjs.scfcloud.task","com.zhjs.scfcloud.model","com.zhjs.scfcloud.util"})
public class ScfcloudTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudTaskApplication.class, args);
    }

}
