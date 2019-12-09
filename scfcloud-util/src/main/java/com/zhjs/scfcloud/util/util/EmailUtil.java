package com.zhjs.scfcloud.util.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;


/**
 * 邮件工具类
 * @author: dailongting
 * @date:2019/5/30 10:48
 */
@Component
public class EmailUtil {

    @Autowired
    private JavaMailSenderImpl sender;

    /**
     * 发送邮件
     * @param text
     * @param subject
     * @param toMail
     */
    public void sendMail(String text, String subject, String toMail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender.getUsername());
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(text);
        sender.send(message);
    }
}
