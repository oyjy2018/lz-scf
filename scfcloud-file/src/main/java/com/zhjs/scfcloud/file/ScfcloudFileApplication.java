package com.zhjs.scfcloud.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

//引入的scfcloud-model项目因为需要使用mybatis plus 注解，对其进行了依赖，本项目没有DataSource相关配置，所以需要spring boot 注册这个类
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@MapperScan(basePackages = {"com.zhjs.scfcloud.model.mapper"})
@ComponentScan(basePackages = {"com.zhjs.scfcloud.file.*","com.zhjs.scfcloud.util"})
@ServletComponentScan(basePackages = {"com.zhjs.scfcloud.file.config"})
public class ScfcloudFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudFileApplication.class, args);
    }

}
