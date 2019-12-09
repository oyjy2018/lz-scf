package com.zhjs.scfcloud.util.util;

import com.zhjs.scfcloud.util.VO.EmailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-02 22:41
 *
 * @author liuchanghai
 * @create 2019-06-02 22:41
 * @since
 */

@Component
public class EmailTool {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String senderMailAddress;

    @Async
    public void sendMailTemplate(Map<String, Object> valueMap){
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 设置发件人邮箱
            helper.setFrom(senderMailAddress);
            // 设置收件人邮箱
            helper.setTo((String[])valueMap.get("to"));
            // 设置邮件标题
            helper.setSubject(valueMap.get("title").toString());

            // 添加正文（使用thymeleaf模板）
            Context context = new Context();
            context.setVariables(valueMap);
            String content = this.templateEngine.process("inviteNewUser.html", context);
            helper.setText(content, true);

            // 添加附件
            if (valueMap.get("filePathList") != null) {
                String[] filePathList = (String[]) valueMap.get("filePathList");
                for(String filePath: filePathList) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                    helper.addAttachment(fileName, fileSystemResource);
                }
            }

            // 发送邮件
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

//    @Async
//    public void sendMailTemplate(String to, String title, ){
//        MimeMessage mimeMessage = null;
//        try {
//            mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            // 设置发件人邮箱
//            helper.setFrom(senderMailAddress);
//            // 设置收件人邮箱
//            helper.setTo(to);
//            // 设置邮件标题
//            helper.setSubject(title);
//
//            // 添加正文（使用thymeleaf模板）
//            Context context = new Context();
//            context.setVariables(valueMap);
//            String content = this.templateEngine.process("inviteNewUser.html", context);
//            helper.setText(content, true);
//
//            // 添加附件
//            if (valueMap.get("filePathList") != null) {
//                String[] filePathList = (String[]) valueMap.get("filePathList");
//                for(String filePath: filePathList) {
//                    FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
//                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
//                    helper.addAttachment(fileName, fileSystemResource);
//                }
//            }
//
//            // 发送邮件
//            mailSender.send(mimeMessage);
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 发送邮箱验证码
     * @param valueMap
     */
    @Async
    public void sendMailVCodeTemplate(Map<String, Object> valueMap){
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 设置发件人邮箱
            helper.setFrom(senderMailAddress);
            // 设置收件人邮箱
            helper.setTo((String[])valueMap.get("to"));
            // 设置邮件标题
            helper.setSubject(valueMap.get("title").toString());
            // 添加正文（使用thymeleaf模板）
            Context context = new Context();
            context.setVariables(valueMap);
            String content = this.templateEngine.process(valueMap.get("template").toString(), context);
            helper.setText(content, true);
            // 发送邮件
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void commonSendMail(EmailVO emailVO){
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            // 设置发件人邮箱
            helper.setFrom(senderMailAddress);
            // 设置收件人邮箱
            helper.setTo(emailVO.getTo());
            // 设置邮件标题
            helper.setSubject(emailVO.getTitle());
            // 添加正文（使用thymeleaf模板）
            Context context = new Context();
            context.setVariables(emailVO.getVariables());
            helper.setText(this.templateEngine.process(emailVO.getTemplate(), context), true);
            // 发送邮件
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    //处理邮件内容参数
    private Map<String, Object> getBusinessTicketEmailMap(String email, String companyName, Long inquireId) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("to", new String[]{email});
        valueMap.put("title", "【领筑票据交易平台】贵公司接受了"+companyName+"的报价，请知悉。(询价单ID："+inquireId+")");
        valueMap.put("email", email);
        valueMap.put("inquireId", inquireId);
        valueMap.put("company", companyName);
        valueMap.put("acceptedTime", companyName);
        valueMap.put("billAmt", companyName);
        valueMap.put("amount", companyName);
        valueMap.put("accepterName", companyName);
        return valueMap;
    }

    public static void main(String[] args) {
//        File file = new File(System.getProperty("user.dir")+"");
//        try {
//            InputStream in = new FileInputStream(file);
//            byte[] b = new byte[in.available()];
//            in.read(b);
//            System.out.println(b.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(EmailTool.class.getResource("").getPath());
    }

}
