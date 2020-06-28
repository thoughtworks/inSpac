package com.thoughtworks.provider.singpass.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

  static final String IV = "tIo0D7UpXjpxsVvK";
  static final int SECRET_KEY_LENGTH = 24; // 192 bit
  static final String MODE = "AES/CBC/PKCS5PADDING";
  static final String PAD_LETTER = "1";

  public static String encrypt(String strToEncrypt, String secret) throws Exception {
    IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
    SecretKeySpec skeySpec =
        new SecretKeySpec(parseSecretKey(secret).getBytes(StandardCharsets.UTF_8), "AES");

    Cipher cipher = Cipher.getInstance(MODE);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

    return Base64.getEncoder()
        .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
  }

  public static String decrypt(String strToDecrypt, String secret) throws Exception {
    IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
    SecretKeySpec skeySpec =
        new SecretKeySpec(parseSecretKey(secret).getBytes(StandardCharsets.UTF_8), "AES");

    Cipher cipher = Cipher.getInstance(MODE);
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

    return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
  }

  private static String parseSecretKey(String key) throws Exception {
    if (Objects.isNull(key)) {
      throw new Exception("AESUtils: Secret key must not be empty");
    }

    if (key.length() == SECRET_KEY_LENGTH) return key;
    if (key.length() < SECRET_KEY_LENGTH)
      return StringUtils.rightPad(key, SECRET_KEY_LENGTH, PAD_LETTER);

    return key.substring(0, SECRET_KEY_LENGTH);
  }
}
