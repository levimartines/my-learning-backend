package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.security.PrincipalService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeGeneratorService {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.profiles.active}")
    private String environment;
    @Value("${qr-generator.url}")
    private String url;

    private static final String SIZE = "200x200";
    private static final String TYPE = "qr";

    public String generateQRUrl() {
        String data = getUrlEncodedData();
        return getUrlWithParams(data);
    }

    private String getUrlWithParams(String data) {
        return String.format("%s?cht=%s&chs=%s&chl=%s", url, TYPE, SIZE, data);
    }

    private String getUrlEncodedData() {
        User user = PrincipalService.getUser();
        String data = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s",
            appName, user.getEmail(), user.getMfaSecret(), getIssuer());
        return URLEncoder.encode(data, StandardCharsets.UTF_8);
    }

    private String getIssuer() {
        return appName + "-" + environment;
    }
}
