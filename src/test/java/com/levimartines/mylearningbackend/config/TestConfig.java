package com.levimartines.mylearningbackend.config;

import com.levimartines.mylearningbackend.properties.CryptoProperties;
import com.levimartines.mylearningbackend.services.email.EmailService;
import com.levimartines.mylearningbackend.services.email.MockEmailService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public EmailService emailService(JavaMailSender mailSender) {
        return new MockEmailService(mailSender, "test@test.com", "", new CryptoProperties("myTestSecret", "myTestSalt"));
    }
}
