package com.levimartines.mylearningbackend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptoTest {

    @Test
    void shouldEncryptAndDecrypt() {
        String value = "mySecretValue";
        String secret = "secret";
        String salt = "salt";
        String encryptedValue = Crypto.encrypt(value, secret, salt);
        String decryptedValue = Crypto.decrypt(encryptedValue, secret, salt);
        assertEquals(value, decryptedValue);
    }

}