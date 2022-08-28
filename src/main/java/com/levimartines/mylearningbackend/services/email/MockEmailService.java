package com.levimartines.mylearningbackend.services.email;

import com.levimartines.mylearningbackend.properties.CryptoProperties;

import lombok.extern.slf4j.Slf4j;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
public class MockEmailService extends AbstractEmailService {

    public MockEmailService(JavaMailSender mailSender, String sender, String frontendHost, CryptoProperties cryptoProperties) {
        super(mailSender, sender, frontendHost, cryptoProperties);
    }

    @Override
    public void sendEmail(MimeMessage message) {
        String[] data = getMessageData(message);
        log.info("Mocking email to: [{}], subject: [{}], text: [{}]", data[0], data[1], data[2]);
    }

}
