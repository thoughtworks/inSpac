package org.keycloak.provider.singpass.SingpassResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/** JWE Compact Serialization : https://tools.ietf.org/html/rfc7516#section-7.1 */
public class SingpassJWEResponse {
  @JsonProperty("recipients")
  public ArrayList<EncryptedKey> encryptedKeys;

  @JsonProperty("protected")
  public String header;

  @JsonProperty("iv")
  public String initializationVector;

  @JsonProperty("ciphertext")
  public String cipherText;

  @JsonProperty("tag")
  public String authenticationTag;

  @Override
  public String toString() {
    return this.header
        + "."
        + this.encryptedKeys.get(0).encryptedKey
        + "."
        + this.initializationVector
        + "."
        + this.cipherText
        + "."
        + this.authenticationTag;
  }

  public static class EncryptedKey {
    @JsonProperty("encrypted_key")
    public String encryptedKey;
  }
}
