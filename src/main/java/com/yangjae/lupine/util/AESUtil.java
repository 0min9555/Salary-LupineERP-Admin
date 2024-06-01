package com.yangjae.lupine.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int T_LEN = 128; // GCM 태그 길이

    @Value("${aes.secret-key}")
    private String secretKey;

    private static final SecureRandom secureRandom = new SecureRandom();

    public String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        byte[] iv = new byte[12]; // GCM 기본 IV 길이
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(T_LEN, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());

        byte[] encryptedIvAndMessage = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedIvAndMessage, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedIvAndMessage, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndMessage);
    }

    public String decrypt(String encryptedInput) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] decodedInput = Base64.getDecoder().decode(encryptedInput);

        byte[] iv = new byte[12];
        System.arraycopy(decodedInput, 0, iv, 0, iv.length);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(T_LEN, iv);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
        byte[] decryptedBytes = cipher.doFinal(decodedInput, iv.length, decodedInput.length - iv.length);

        return new String(decryptedBytes);
    }
}
