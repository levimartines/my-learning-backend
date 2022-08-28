package com.levimartines.mylearningbackend.utils;

import com.levimartines.mylearningbackend.exceptions.CryptoException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Crypto {
    private static final IvParameterSpec parameterSpec = generateInitializationVector();

    public static String encrypt(String valueToEncrypt, String secret, String salt) {
        try {
            Cipher cipher = getCipherInstance();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey(secret, salt), parameterSpec);
            byte[] cipherText = cipher.doFinal(valueToEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception ex) {
            log.error("Error while encrypting value [{}]", valueToEncrypt);
            throw new CryptoException("Error encrypting/decrypting");
        }
    }

    public static String decrypt(String encryptedValue, String secret, String salt) {
        try {
            Cipher cipher = getCipherInstance();
            cipher.init(Cipher.DECRYPT_MODE, secretKey(secret, salt), parameterSpec);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(plainText);
        } catch (Exception ex) {
            log.error("Error while decrypting value [{}]", encryptedValue);
            throw new CryptoException("Error encrypting/decrypting");
        }
    }

    private static Cipher getCipherInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    private static SecretKey secretKey(String secret, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static IvParameterSpec generateInitializationVector() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
