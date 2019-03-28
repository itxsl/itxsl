package cn.itxsl.kernel.repository;

import cn.itxsl.kernel.model.email.Email;
import cn.itxsl.kernel.model.mapped.ITEmail;
import cn.itxsl.kernel.repository.base.Repository;
import cn.itxsl.kernel.utils.RunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/20 16:42
 * @description：邮件管理
 */
@Async
@Component
public class EmailRepository extends Repository<ITEmail> {

    @Autowired
    private Email email;

    public void sendEmail(ITEmail sendEmail) {
        if (sendEmail.getContent()!=null){
            try {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                Properties proper = new Properties();
                proper.setProperty(email.getAuthKey(), email.getAuth());
                proper.setProperty(email.getTimeoutkey(), email.getTimeout());
                proper.setProperty(email.getLoglevelkey(), email.getLoglevel());
                mailSender.setJavaMailProperties(proper);
                mailSender.setHost(email.getHost());
                mailSender.setUsername(email.getUsername());
                mailSender.setPassword(email.getPassword());
                mailSender.setPort(email.getPort());
                mailSender.setDefaultEncoding(email.getCode());
                mailSender.setProtocol(email.getProtocol());
                MimeMessage mailMessage = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
                // 设置收件人，寄件人 用数组发送多个邮件
                messageHelper.setTo(sendEmail.getEmail());
                messageHelper.setFrom(RunUtils.getCurrentUser().getEmail());
                messageHelper.setSubject(sendEmail.getSubject());
                MimeMessage message = messageHelper.getMimeMessage();
                Multipart mainPart = new MimeMultipart();
                // 创建一个包含HTML内容的MimeBodyPart
                BodyPart html = new MimeBodyPart();
                html.setContent(sendEmail.getContent(), "text/html; charset=utf-8");
                mainPart.addBodyPart(html);
                message.setContent(mainPart);
                mailSender.send(message);
                sendEmail.setStatus("发送成功！");
                post(sendEmail);
            } catch (Exception e) {
                logger.error("邮件发送异常:{}", e.getMessage());
                sendEmail.setStatus("发送失败！");
                post(sendEmail);
            }

        }else {
            sendEmail.setStatus("发送失败！");
            post(sendEmail);
        }
    }

}
