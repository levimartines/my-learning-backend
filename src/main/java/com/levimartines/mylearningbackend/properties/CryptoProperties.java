package com.levimartines.mylearningbackend.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crypto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptoProperties {
    private String secret;
    private String salt;
}
