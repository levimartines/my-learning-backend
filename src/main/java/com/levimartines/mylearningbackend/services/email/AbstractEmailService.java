package com.levimartines.mylearningbackend.services.email;

import com.levimartines.mylearningbackend.exceptions.EmailException;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.properties.CryptoProperties;
import com.levimartines.mylearningbackend.utils.Crypto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
public abstract class AbstractEmailService implements EmailService {

    protected final JavaMailSender mailSender;
    protected final String sender;
    protected final String frontendHost;
    protected final String secret;
    protected final String salt;

    protected AbstractEmailService(JavaMailSender mailSender, String sender, String frontendHost,
                                   CryptoProperties properties) {
        this.mailSender = mailSender;
        this.sender = sender;
        this.frontendHost = frontendHost;
        this.secret = properties.getSecret();
        this.salt = properties.getSalt();
    }

    @Override
    public void sendConfirmAccountEmail(User user) {
        MimeMessage message = buildMessage(user);
        sendEmail(message);
    }

    protected MimeMessage buildMessage(User user) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String subject = "Account confirmation";
            helper.setTo(user.getEmail());
            InternetAddress from = new InternetAddress(sender, "Levi Ferreira");
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setSentDate(new Date(System.currentTimeMillis()));
            helper.setText(getConfirmationText(user), true);
            return helper.getMimeMessage();
        } catch (Exception ex) {
            log.error("Error creating confirmation email message to [{}]", user.getEmail());
            throw new EmailException("Error creating confirmation email");
        }
    }

    protected String getConfirmationText(User user) {
        String encryptedSecret = Crypto.encrypt(String.valueOf(user.getId()), secret, salt);
        String encodeSecret = URLEncoder.encode(encryptedSecret, StandardCharsets.UTF_8);
        String href = frontendHost + "/confirm-registration?code=" + encodeSecret;
        String message = """
            <div>
                <h3>Thanks for joining my-learning app!</h3>
                <p>Click <a href="%s">HERE</a> to confirm your registration
                </p>
                <br>
                <p style="font-size: 11px">If you did not request this email, please just ignore it.</p>
            </div>
                        """;
        return String.format(message, href);
    }

    protected String[] getMessageData(MimeMessage message) {
        try {
            String to = message.getAllRecipients()[0].toString();
            String subject = message.getSubject();
            String text = message.getContent().toString();
            return new String[]{to, subject, text};
        } catch (Exception exception) {
            return new String[]{"", "", ""};
        }
    }
}
