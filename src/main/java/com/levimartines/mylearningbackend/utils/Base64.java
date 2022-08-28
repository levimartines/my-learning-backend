package com.levimartines.mylearningbackend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Base64 {

    public static String encode(byte[] value) {
        return java.util.Base64.getEncoder().encodeToString(value);
    }
}
