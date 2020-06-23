package com.thoughtworks.provider.singpass;

import java.util.Arrays;
import java.util.List;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/** @author yuexie.zhou */
public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);
  }

  public SingpassIdentityProviderConfig() {
    this.setValidateSignature(true);
    this.setUseJwksUrl(false);
  }

  public boolean isSupportCP() {
    return Boolean.parseBoolean(this.getConfig().get("supportCP"));
  }

  public boolean isAutoRegister() {
    return Boolean.parseBoolean(this.getConfig().get("autoRegister"));
  }

  public String getFirstNameBySub(String subject) {
    String firstNameLinkToType = getConfig().get("firstNameLinkToType");

    return LinkToType.fromString(firstNameLinkToType).parse(subject);
  }

  public String getLastNameBySub(String subject) {
    String lastNameLinkToType = getConfig().get("lastNameLinkToType");

    return LinkToType.fromString(lastNameLinkToType).parse(subject);
  }

  public String getUserNameBySub(String subject) {
    String userNameLinkToType = getConfig().get("userNameLinkToType");

    return LinkToType.fromString(userNameLinkToType).parse(subject);
  }

  public void setPrivateKey(String privateKey) {
    getConfig().put("privateKey", privateKey);
  }

  public String getPrivateKey() {
    return getConfig().get("privateKey");
  }

  public enum LinkToType {
    NRIC("s"),
    UUID("u"),
    SUB("sub");

    public static final String DEFAULT_VAL = "NONE";
    private static final String TYPE_SPLIT_SIGN = ",";
    private static final String KEY_VALUE_SPLIT_SIGN = "=";
    private String type;

    LinkToType(String type) {
      this.type = type;
    }

    public static LinkToType fromString(String str) {
      for (LinkToType toType : LinkToType.values()) {
        if (toType.name().equals(str)) return toType;
      }


      return LinkToType.SUB;
    }

    public String parse(String subject) {
      if (this.equals(LinkToType.SUB)) {
        return subject;
      }

      List<String> subPart = Arrays.asList(subject.split(TYPE_SPLIT_SIGN));

      return subPart.stream()
          .filter(part -> part.contains(this.type + KEY_VALUE_SPLIT_SIGN))
          .flatMap(part -> Arrays.stream(part.split(KEY_VALUE_SPLIT_SIGN)))
          .skip(1)
          .findFirst()
          .orElse(DEFAULT_VAL);
    }
  }
}
