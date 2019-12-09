package com.zhjs.scfcloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ScfcloudConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScfcloudConfigApplication.class, args);
    }

}
