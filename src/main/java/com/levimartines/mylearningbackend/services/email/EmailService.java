package com.levimartines.mylearningbackend.services.email;

import com.levimartines.mylearningbackend.models.entities.User;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void sendConfirmAccountEmail(User user);

    void sendEmail(MimeMessage message);
}
