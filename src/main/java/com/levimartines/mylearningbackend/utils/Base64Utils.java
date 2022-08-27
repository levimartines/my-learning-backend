package com.levimartines.mylearningbackend.utils;

import java.util.Base64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64Utils {

    public static String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }
}
