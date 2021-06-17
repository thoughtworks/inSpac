package com.thoughtworks.inspac.keycloak.representations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Arrays;
import org.keycloak.json.StringOrArrayDeserializer;
import org.keycloak.json.StringOrArraySerializer;
import org.keycloak.representations.JsonWebToken;

public class SingpassJsonWebToken extends JsonWebToken {
  @JsonProperty("aud")
  @JsonSerialize(using = StringOrArraySerializer.class)
  @JsonDeserialize(using = StringOrArrayDeserializer.class)
  protected String[] aud;

  @JsonIgnore
  public String[] getAud() {
    return this.aud;
  }

  @JsonIgnore
  public String[] getAudience() {
    return this.aud;
  }

  @Override
  public boolean hasAudience(String audience) {
    if (this.aud == null) {
      return false;
    } else {
      String[] var2 = this.aud;
      int var3 = var2.length;

      for (int var4 = 0; var4 < var3; ++var4) {
        String a = var2[var4];
        if (a.equals(audience)) {
          return true;
        }
      }

      return false;
    }
  }

  @Override
  public JsonWebToken audience(String... audience) {
    this.audience = audience;
    this.aud = audience;
    return this;
  }

  @Override
  public JsonWebToken addAudience(String audience) {
    if (this.aud == null) {
      this.aud = new String[] {audience};
    } else {
      String[] newAudience = this.aud;
      int var3 = newAudience.length;

      for (int var4 = 0; var4 < var3; ++var4) {
        String aud = newAudience[var4];
        if (audience.equals(aud)) {
          return this;
        }
      }

      newAudience = (String[]) Arrays.copyOf(this.aud, this.aud.length + 1);
      newAudience[this.aud.length] = audience;
      this.aud = newAudience;
    }
    return this;
  }
}
