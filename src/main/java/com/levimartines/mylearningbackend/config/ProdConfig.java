package com.levimartines.mylearningbackend.config;

import com.levimartines.mylearningbackend.properties.CryptoProperties;
import com.levimartines.mylearningbackend.services.email.EmailService;
import com.levimartines.mylearningbackend.services.email.SmtpEmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public EmailService emailService(JavaMailSender mailSender, @Value("${mail.sender}") String sender,
                                     @Value("${service.frontend-host}") String frontendHost, CryptoProperties cryptoProperties) {
        return new SmtpEmailService(mailSender, sender, frontendHost, cryptoProperties);
    }
}
