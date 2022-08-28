package com.levimartines.mylearningbackend.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user.picture")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageProperties {
    private String imagePrefix;
    private String imageSize;
}
