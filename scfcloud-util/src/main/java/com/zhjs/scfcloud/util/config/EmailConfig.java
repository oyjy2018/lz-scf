package com.zhjs.scfcloud.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件配置
 * @author: dailongting
 * @date:2019/5/30 11:40
 */
@Configuration
public class EmailConfig {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private int port;
    @Value("${mail.protocol}")
    private String protocol;
    @Value("${mail.default-encoding}")
    private String default_encoding;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.smtp.auth}")
    private String smtp_auth;
    @Value("${mail.smtp.starttls.enable}")
    private String smtp_starttls_enable;
    @Value("${mail.smtp.starttls.required}")
    private String smtp_starttls_required;
    @Value("${mail.smtp.ssl.enable}")
    private String smtp_ssl_enable;
    @Value("${mail.debug}")
    private String debug;

    @Bean
    public JavaMailSenderImpl sender(){
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth",smtp_auth);
        p.setProperty("mail.smtp.starttls.enable",smtp_starttls_enable);
        p.setProperty("mail.smtp.starttls.required",smtp_starttls_required);
        p.setProperty("mail.smtp.ssl.enable",smtp_ssl_enable);
        p.setProperty("mail.debug",debug);
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setProtocol(protocol);
        sender.setDefaultEncoding(default_encoding);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setJavaMailProperties(p);
        return sender;
    }
}
