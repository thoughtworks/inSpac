package com.thoughtworks.inspac.keycloak.utils;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class AESUtilsTest {
  public static final String SECRET_KEY_16 = "tIo0D7UpXjpxsVvKo9o9";
  public static final String SECRET_KEY_24 = "tIo0D7UpXjpxsVvKo9o91111";
  public static final String SECRET_KEY_32 = "tIo0D7UpXjpxsVvKo9o9111111111111";

  @Test
  void should_success_when_encrypt_content() throws Exception {
    // when
    String encrypt = AESUtils.encrypt("1", SECRET_KEY_24);

    assertEquals(encrypt, "grl8CVPruk/r3jrMPi92DQ==");
  }

  @Test
  void should_success_when_decrypt_content() throws Exception {
    // given
    String encryptedText = "grl8CVPruk/r3jrMPi92DQ==";

    // when
    String decrypt = AESUtils.decrypt(encryptedText, SECRET_KEY_24);

    // then
    assertEquals(decrypt, "1");
  }

  @Test
  void should_success_when_encrypt_and_decrypt() throws Exception {
    // given
    String content =
        IOUtils.toString(this.getClass().getResourceAsStream("/keycloakPrivateKey.pem"), "UTF-8");

    // when
    String encryptText = AESUtils.encrypt(content, SECRET_KEY_24);
    String decryptText = AESUtils.decrypt(encryptText, SECRET_KEY_24);

    // then
    assertEquals(content, decryptText);
  }

  @Test
  void should_success_when_encrypt_and_decrypt_given_secret_key_length_less_than_24()
      throws Exception {
    // given
    String content = "1";

    // when
    String encryptText = AESUtils.encrypt(content, SECRET_KEY_16);
    String decryptText = AESUtils.decrypt(encryptText, SECRET_KEY_16);

    // then
    assertEquals(content, decryptText);
  }

  @Test
  void should_success_when_encrypt_and_decrypt_given_secret_key_length_greater_24()
      throws Exception {
    // given
    String content = "1";

    // when
    String encryptText = AESUtils.encrypt(content, SECRET_KEY_32);
    String decryptText = AESUtils.decrypt(encryptText, SECRET_KEY_32);

    // then
    assertEquals(content, decryptText);
  }

  @Test
  void should_throw_exception_when_secret_key_is_null() {
    // given
    String content = "1";

    // when
    Assertions.assertThrows(
        Exception.class,
        () -> AESUtils.encrypt(content, null),
        "AESUtils: Secret key must not be empty");
  }
}
