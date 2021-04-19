package com.thoughtworks.provider.singpass.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.security.PrivateKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class PrivateKeyUtilsTest {
  @Test
  void should_success_when_parse_private_key() throws IOException {
    String privateKeyString = "-----BEGIN PRIVATE KEY-----\n"
        + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7di8TGHWqMkGm\n"
        + "SMAsp8K4HrsqqPMJz8TBSsE6R2gRg0u6vzQDZjdHi/arUBbx/x7dsyI8stKW/FLR\n"
        + "rRjJqJPb7j8Q2PDL51RrWj1d2JYuYdZYxaLuN7UgrjOwPGgi32dI7FpmaG7hpl4/\n"
        + "4aGsyJUudXTEoOX/ifZBlsPUaR2Qbhm7KocmyYZwa1ctTVTqvqnpkOsJOQFeNf3o\n"
        + "QOhsd9dUV+L9EntU4PZo/angbsjXTi00FiYH36aQevgC45P0+GY6qrhr0WjUTpUu\n"
        + "eWjgVb/ZkrXSBRTR/nBpyc2KWURHQ0Y3AVahOPYBIX0uJHr/f1vixq1EsDLxHExy\n"
        + "W9ryIrrJAgMBAAECggEAY8jsE+kQMRFhWqcdDGgcQS+yh2m5PP7Ih+9H3cLGxZOz\n"
        + "CuvePvT49e+t1NDj9drMTkydK9wwNsiHOS8/o5BFbGtsTIZ93rv7ds1pHvw8LOJN\n"
        + "W6GQMeebVZME1onBENcEPo/5KsvqQdjyEGUFT1jR+BHznvrakuSYHZ+oC/gMEaVw\n"
        + "dUsM1849EbLDJ5lOPDDqYwsJwIGEryxRLFP+4HhGR9wnrTVee5CCsH0ep8OqypvY\n"
        + "xgg9Ytyt1WripwzhXsVzJxahTbO4XImOgv6Uvo3EYfBXm1gbfugGbSiyYHBBx0Pa\n"
        + "7DtYrpRzjeS33m6Y4SJjKERjCbHXChwUrFcMGS5ogQKBgQDeChbIHYRu4HTQYoV9\n"
        + "pJ70LcHCiE0zaJzuMHes4OFkNAuFXASDGp1HJXd0tol+oeuYO/q1gENvyfBAqDH/\n"
        + "AVAtWaFvQUNzv/Zs++mbrSSwxuZJCaJAbh9GKzCyCbapxBtdzhgCLLY9GeWRiG1c\n"
        + "puYUUvJmkcWQE9sSn2tnE8mv2QKBgQDYIjWgagTLPlT3J65t5BEs9fwjAq8bdIIV\n"
        + "E9o0blIwlKF3FzeUo4Egjc/vfIYOL43AOttuVbVoruvvT13a3+VpCXjxqSYnveuw\n"
        + "FB0xQC1F0c3ay4Oprjh2qk2GfOy0gikaRgmTUP3nVK1LC57GWJMker/UQ2kWCJCs\n"
        + "RyMzpgd8cQKBgQCE5q8KKrjJEOp6jG3wbWeDKhwuzxy+Z6B+5V3MiXH/YzN+KDy/\n"
        + "KF/5ZNCieFvGAy8cGNKQbuxubgWy/bmnM+cErgB1si+oib77LrF+L92lPfg6wVxv\n"
        + "ijqH6nQkLLI73RiwRhqSuqZ93hFN0cX7zh4rDhbvE9OX0HqxI+DKesqeyQKBgEln\n"
        + "hPMQTsSATPcMAQ/Nb4/nk1SIqtQWQ7/I2EkKVtus/xGlTvkqdsaJo19g2V6kA+6P\n"
        + "jsrwTQZaskK6n9OgSxfbYbohipXgyNUqX6fEdhvKX7G5gOP2CbMzr9THRNUhh7gm\n"
        + "pUXlMfaJKbndHnWay46OKex7YItdKVV5a5k1AEHhAoGBAIVSqdn2f4mVgdQxDls6\n"
        + "DPp0XJW2D9nI9w9mm+wZc9TtI7X+7+1LZ609vDHAEfj8flTMl0t/ovaaZWmEHCpg\n"
        + "FKM9rIqep3tZy6qqgo63peDk+RPV3UVgnUwq0+mBwhnHPWQf2nfYnZZHwsldfHWP\n"
        + "y0U6Qc7UNI3EjJEJPTI4Zifg\n"
        + "-----END PRIVATE KEY-----";

    PrivateKey privateKey = PrivateKeyUtils.parsePrivateKey(privateKeyString);

    assertEquals(privateKey.getFormat(), "PKCS#8");
  }
}