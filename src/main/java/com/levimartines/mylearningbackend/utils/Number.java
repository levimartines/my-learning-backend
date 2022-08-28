package com.levimartines.mylearningbackend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Number {

    public static boolean isValidLong(String code) {
        try {
            if (code == null) return false;
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
