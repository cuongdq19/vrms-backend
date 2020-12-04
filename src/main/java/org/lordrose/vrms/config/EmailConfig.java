package org.lordrose.vrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender initJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("vrms.cskh@gmail.com");
        mailSender.setPassword("Vrms.kiemhh");

        Properties senderProps = mailSender.getJavaMailProperties();
        senderProps.put("mail.transport.protocol", "smtp");
        senderProps.put("mail.smtp.auth", "true");
        senderProps.put("mail.smtp.starttls.enable", "true");
        senderProps.put("mail.debug", "true");

        return mailSender;
    }
}
